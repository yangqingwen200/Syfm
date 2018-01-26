<%@ page language="java" contentType="text/html; charset=utf-8" %>
<div class="row">
    <div class="col-md-7">
        <div style="float:right;">
            <nav id="page_forward">
                <ul class="pagination" style="margin: 10px">
                    <c:if test="${page.pageNow eq 1}">
                        <li class="disabled"><a>首页</a></li>
                        <li class="disabled">
                            <a aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${page.pageNow ne 1}">
                        <li><a id="page_forward_first" style="cursor: pointer" value="1">首页</a></li>
                        <li>
                            <a id="page_forward_previous" style="cursor: pointer" aria-label="Previous" value="${page.pageNow - 1}" title="上一页">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach items="${page.continuePage}" var="continuePage" varStatus="i">
                        <li <c:if test="${page.pageNow eq continuePage}">class="active"</c:if>>
                            <a id="page_forward_${i.index + 1}" style="cursor: pointer" value="${continuePage}" title="第${continuePage}页">
                                    ${continuePage}
                            </a>
                        </li>
                    </c:forEach>

                    <c:if test="${page.pageNow eq page.pageCount}">
                        <li class="disabled">
                            <a aria-label="Previous">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                        <li class="disabled"><a>尾页</a></li>
                    </c:if>
                    <c:if test="${page.pageNow ne page.pageCount}">
                        <li>
                            <a id="page_forward_next" style="cursor: pointer" aria-label="Next" value="${page.pageNow + 1}" title="下一页">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                        <li><a id="page_forward_end" style="cursor: pointer" value="${page.pageCount}">尾页</a></li>
                    </c:if>
                </ul>
            </nav>
        </div>

    </div>
    <div class="col-md-5">
        <div style="float:left; margin-top:10px;">
            第<input id="page_forward_go_page" type="text" class="form-control page-forward-go-page" value="${page.pageNow}" pageCount="${page.pageCount}"/>/ ${page.pageCount} 页, 每页
            <select id="page_forward_setpagesize" class="form-control page-forward-setpagesize">
                <option <c:if test="${page.pageSize eq 10}">selected="selected"</c:if> value="10">10</option>
                <option <c:if test="${page.pageSize eq 15}">selected="selected"</c:if> value="15">15</option>
                <option <c:if test="${page.pageSize eq 20}">selected="selected"</c:if> value="20">20</option>
                <option <c:if test="${page.pageSize eq 30}">selected="selected"</c:if> value="30">30</option>
                <option <c:if test="${page.pageSize eq 40}">selected="selected"</c:if> value="40">40</option>
                <option <c:if test="${page.pageSize eq 50}">selected="selected"</c:if> value="50">50</option>
            </select>
            条, 显示第
                <c:if test="${page.totalNum ne 0}">
                    ${(page.pageNow - 1) * page.pageSize + 1}
                </c:if>
                <c:if test="${page.totalNum eq 0}">
                    0
                </c:if>
                -
                <c:if test="${page.pageNow * page.pageSize >= page.totalNum}">
                    ${page.totalNum}
                </c:if>
                <c:if test="${page.pageNow * page.pageSize < page.totalNum}">
                    ${page.pageNow * page.pageSize}
                </c:if> 条记录, 共 ${page.totalNum} 条记录
        </div>
   </div>

</div>
