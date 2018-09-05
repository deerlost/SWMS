package com.inossem.wms.model.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.util.StringUtils;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PageInterceptor implements Interceptor {
	private static final String SELECT_ID_SUFFIX = "onpaging";

	// 插件运行的代码，它将代替原有的方法
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// System.out.println("PageInterceptor -- intercept");

		if (invocation.getTarget() instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String selectId = mappedStatement.getId();

			if (selectId.substring(selectId.lastIndexOf(".") + 1).toLowerCase().endsWith(SELECT_ID_SUFFIX)) {
				BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
				// 分页参数作为参数对象parameterObject的一个属性
				String sqlOriginal = boundSql.getSql().trim();
				String sqlLowercase = sqlOriginal.toLowerCase();

				Object paramteterObject = boundSql.getParameterObject();

				PreparedStatement countStmt = null;
				ResultSet rs = null;
				int totalCount = -1;
				try {

					String pageSql;
					int pageIndex = 1;
					int offset = 0;
					int pageSize = 10;
					boolean paging = false;
					boolean sortAscend = true;
					String sortColumn = null;
					List<PageAggregate> aggregateColumns = null;
					if (paramteterObject instanceof HashMap) {
						HashMap<String, Object> map = (HashMap<String, Object>) paramteterObject;
						if (map.containsKey("paging")) {
							paging = (Boolean) (map.get("paging"));
						}

						if (map.containsKey("totalCount")) {
							totalCount = (Integer) (map.get("totalCount"));
						}

						if (map.containsKey("pageIndex")) {
							pageIndex = (Integer) (map.get("pageIndex"));
						}

						if (map.containsKey("pageSize")) {
							pageSize = (Integer) (map.get("pageSize"));
						}
						if (map.containsKey("sortAscend")) {
							sortAscend = (Boolean) (map.get("sortAscend"));
						}
						if (map.containsKey("sortColumn")) {
							sortColumn = (String) (map.get("sortColumn"));
						}
						if (map.containsKey("aggregateColumns")) {
							aggregateColumns = (List<PageAggregate>) (map.get("aggregateColumns"));
						}

					} else {
						IPageCommon pageCommon = (IPageCommon) paramteterObject;
						paging = pageCommon.isPaging();
						totalCount = pageCommon.getTotalCount();
						pageIndex = pageCommon.getPageIndex();
						pageSize = pageCommon.getPageSize();
						sortAscend = pageCommon.isSortAscend();
						sortColumn = pageCommon.getSortColumn();
						aggregateColumns = pageCommon.getAggregateColumns();
					}

					if (StringUtils.hasText(sortColumn)) {
						sortColumn.replace('#', ' ');
						sortColumn.replace('-', ' ');
						sortColumn.replace('\'', ' ');
					}

					if (paging) {

						if (totalCount < 0) {
							// 重写sql
							String countSql = concatCountSql(sqlOriginal, sqlLowercase, aggregateColumns);

							Connection connection = (Connection) invocation.getArgs()[0];

							ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
									paramteterObject, boundSql);

							countStmt = connection.prepareStatement(countSql);
							// 通过parameterHandler给PreparedStatement对象设置参数
							parameterHandler.setParameters(countStmt);
							// 之后就是执行获取总记录数的Sql语句和获取结果了。
							rs = countStmt.executeQuery();
							if (rs.next()) {
								totalCount = rs.getInt("totalCount");
								if (aggregateColumns != null && aggregateColumns.size() > 0) {
									for (PageAggregate pageAggregate : aggregateColumns) {
										pageAggregate.setValue(rs.getString(pageAggregate.getAlias()));
									}
								}
							}

							// System.out.println("重写的 count sql :" + countSql);
						}

						if (pageIndex <= 0) {
							offset = 0;
						} else {
							offset = (pageIndex - 1) * pageSize;
							if (offset >= totalCount && totalCount > 0) {
								offset -= pageSize;
							}
						}

						pageSql = concatPageSql(sqlOriginal, sqlLowercase, aggregateColumns, totalCount, offset,
								pageSize, sortAscend, sortColumn);

						// System.out.println("重写的 select sql :" + pageSql);
						metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
					}
				} catch (SQLException e) {
					System.out.println("Ignore this exception" + e);
				} catch (Exception e) {
					throw e;
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (countStmt != null) {
							countStmt.close();
						}
					} catch (SQLException e) {
						System.out.println("Ignore this exception" + e);
					}
				}
			}
		}

		return invocation.proceed();
	}

	/**
	 * 拦截类型StatementHandler
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * 组合统计条数的SQL
	 * 
	 * @param sqlOriginal
	 *            原SQL
	 * @param sqlLowercase
	 *            小写SQL
	 * @return
	 */
	private String concatCountSql(String sqlOriginal, String sqlLowercase, List<PageAggregate> aggregateColumns) {
		int union = PageHelper.firstKeyword(sqlLowercase, PageHelper.UNION, 0);
		int group = PageHelper.firstKeyword(sqlLowercase, PageHelper.GROUP, 0);
		int distinct = PageHelper.firstKeyword(sqlLowercase, PageHelper.DISTINCT, 0);
		int firstParameter = sqlLowercase.indexOf("?");
		int topFrom = PageHelper.topFrom(sqlLowercase);
		int orderIndex = PageHelper.lastKeyword(sqlLowercase, PageHelper.ORDER, sqlLowercase.length() - 1);
		boolean order = orderIndex > sqlLowercase.lastIndexOf(")");
		boolean subquery;
		if (union > 0 || group > 0 || distinct > 0) {
			subquery = true;
		} else if (firstParameter > 0 && topFrom > 0 && firstParameter < topFrom) {
			subquery = true;
		} else {
			subquery = false;
		}
		// boolean subquery = sqlLowercase.indexOf("union") > 0;

		StringBuffer sb = new StringBuffer("SELECT COUNT(*) totalCount");
		if (aggregateColumns != null && aggregateColumns.size() > 0) {
			for (PageAggregate aggregateColumn : aggregateColumns) {
				sb.append(",").append(aggregateColumn.getExpression()).append(" ").append(aggregateColumn.getAlias());
			}
		}
		sb.append(" FROM ");
		if (subquery) {

			sb.append("(");
			if (order) {
				// 如果最后的order是排序目的,忽略最后的order
				sb.append(sqlOriginal.substring(0, orderIndex));
			} else {
				sb.append(sqlOriginal);
			}
			sb.append(") t");
		} else {

			if (order) {
				// 如果最后的order是排序目的,忽略最后的order
				sb.append(sqlOriginal.substring(topFrom + 4, orderIndex));
			} else {
				// 如果最后的order是其他目的,直接修改成统计
				sb.append(sqlOriginal.substring(topFrom + 4));
			}
		}

		return sb.toString();
	}

	/**
	 * 组合分页的SQL
	 * 
	 * @param sqlOriginal
	 *            原SQL
	 * @param sqlLowercase
	 *            小写SQL
	 * @param totalCount
	 *            总条数
	 * @param offset
	 *            跳过的条数
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	private String concatPageSql(String sqlOriginal, String sqlLowercase, List<PageAggregate> aggregateColumns,
			int totalCount, int offset, int pageSize, boolean sortAscend, String sortColumn) {
		int union = PageHelper.firstKeyword(sqlLowercase, PageHelper.UNION, 0);
		int group = PageHelper.firstKeyword(sqlLowercase, PageHelper.GROUP, 0);
		int distinct = PageHelper.firstKeyword(sqlLowercase, PageHelper.DISTINCT, 0);
		int firstParameter = sqlLowercase.indexOf("?");
		int topFrom = PageHelper.topFrom(sqlLowercase);
		int orderIndex = PageHelper.lastKeyword(sqlLowercase, PageHelper.ORDER, sqlLowercase.length() - 1);
		boolean order = orderIndex > sqlLowercase.lastIndexOf(")");
		boolean subquery;
		if (union > 0 || group > 0 || distinct > 0) {
			subquery = true;
		} else if (firstParameter > 0 && topFrom > 0 && firstParameter < topFrom) {
			subquery = true;
		} else {
			subquery = false;
		}
		// boolean subquery = sqlLowercase.indexOf("union") > 0;

		StringBuffer sb = new StringBuffer();
		sb.append(sqlOriginal);

		if (StringUtils.hasText(sortColumn) && order) {
			sb.delete(orderIndex, sqlOriginal.length());
		}

		StringBuffer aggregateSQL = new StringBuffer();
		if (aggregateColumns != null && aggregateColumns.size() > 0) {
			for (PageAggregate aggregateColumn : aggregateColumns) {
				aggregateSQL.append(",'").append(aggregateColumn.getValue()).append("' ")
						.append(aggregateColumn.getAlias());
			}
		}

		if (subquery) {
			sb.insert(0, String.format("SELECT *,%d totalCount %s FROM (", totalCount, aggregateSQL));
			// 如果最后的字符是分号,去掉
			if (sb.charAt(sb.length() - 1) == ';') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append(") t");
		} else {

			// 第一个select后面添加总条数字段
			sb.insert(sqlLowercase.indexOf("select") + 6,
					String.format(" %d totalCount %s,", totalCount, aggregateSQL));
			// 如果最后的字符是分号,去掉
			if (sb.charAt(sb.length() - 1) == ';') {
				sb.deleteCharAt(sb.length() - 1);
			}
		}

		if (StringUtils.hasText(sortColumn)) {
			// 当排序列存在时,即需要排序时,
			// 查看列的驼峰式存在,如果存在直接用驼峰式排序,因为驼峰式肯定是别名
			// 如果驼峰式不存在,查看排序列出现的位置,查看是否有前缀表名,
			// 如果有,必须带着前缀表名排序,方式两个表都有这个排序列
			String sortHumpColumn = PageHelper.humpName(sortColumn);
			// int firstSortHumpColumnIndex =
			// sqlOriginal.indexOf(sortHumpColumn);
			PageWord wordForHump = PageHelper.firstColumn(sqlOriginal, sortHumpColumn, 0);
			PageWord word;
			if (wordForHump != null && wordForHump.getBegin() > 0 && wordForHump.getBegin() < topFrom) {
				sb.append(" ORDER BY ").append(sortHumpColumn);
				if (sortAscend) {
					sb.append(" ASC");
				} else {
					sb.append(" DESC");
				}
			} else {
				word = PageHelper.firstColumn(sqlOriginal, sortColumn, 0);
				if (word != null && word.getBegin() < topFrom) {
					if (subquery) {
						sb.append(" ORDER BY ").append(sortColumn);
					} else {
						sb.append(" ORDER BY ")
								.append(sqlOriginal.subSequence(word.getBegin(), word.getBegin() + word.getLength()));
					}
					if (sortAscend) {
						sb.append(" ASC");
					} else {
						sb.append(" DESC");
					}
				}
			}
		}
		sb.append(" LIMIT ").append(offset).append(',').append(pageSize).append(';');
		return sb.toString();
	}

}
