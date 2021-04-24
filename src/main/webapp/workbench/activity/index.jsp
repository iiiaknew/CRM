<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">

    <link href="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css"
          rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"
          type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {

            // 为创建按钮添加事件 打开添加操作的模态窗口
            $("#addbtn").click(function () {

                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                $.ajax({
                    url: "${pageContext.request.contextPath}/workbench/activity/getUserList.do",
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        var html = "";
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'>" + n.name + "</option>";
                        })
                        $("#create-owner").html(html);
                        // 展示创建市场活动的模态窗口
                        $("#createActivityModal").modal("show");

                        // 将当前登录的用户设置为默认选项
                        var id = "${sessionScope.user.id}";
                        $("#create-owner").val(id);
                    }
                })
            })

            // 为保存按钮添加时间
            $("#saveBtn").click(function () {
                $.ajax({
                    url: "${pageContext.request.contextPath}/workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-owner").val()),
                        "name": $.trim($("#create-name").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-description").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            // 添加成功 局部刷新市场活动信息列表
                            //pageList(1, 2);
                            /*
                            *   $("#activityPage").bs_pagination('getOption', 'currentPage')
                            *   操作后提留在当前页
                            *
                            *   $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            *   操作后维持已经设置好的每页展现的记录数
                            *
                            * */
                            // 添加成功 返回到第一页 维持已经设置好的每页展现的记录数
                            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            // 删除表单数据
                            $("#activityAddForm")[0].reset();
                            // 关闭模态窗口
                            $("#createActivityModal").modal("hide");
                        } else {
                            alert("添加失败");
                        }
                    }
                })
            })
            // 页面处理完毕 触发pageList方法
            pageList(1, 2);

            // 为修改按钮绑定事件
            $("#editBtn").click(function (){
                if ($("input[name=xz]:checked").length > 1){
                    alert("请选择一个");
                }else if($("input[name=xz]:checked").length == 0){
                    alert("请选择要修改的记录");
                } else {

                    var id = $("input[name=xz]:checked").val();
                    var param = "id=" + id;
                    //alert(param);


                    $.ajax({
                        url : "${pageContext.request.contextPath}/workbench/activity/getUserListAndActivity.do",
                        data : param,
                        type : "get",
                        dataType : "json",
                        success : function (data){
                            // data : {“用户列表”:[{1}，{2}，{3}]， ”activity“ : 信息}

                            var html = "";
                            $.each(data.users, function (i, n) {
                                html += "<option value='" + n.id + "'>" + n.name + "</option>";
                            })
                            $("#edit-owner").html(html);

                            // 传回activity对象
                            $("#edit-name").val(data.activity.name);
                            $("#edit-startDate").val(data.activity.startDate);
                            $("#edit-endDate").val(data.activity.endDate);
                            $("#edit-cost").val(data.activity.cost);
                            $("#edit-description").val(data.activity.description);
                            // 把修改的市场活动id存到隐藏域
                            $("#edit-id").val(data.activity.id);
                            //  打开模态窗口
                            $("#editActivityModal").modal("show");
                        }
                    })
                }
            })

            // 为更新按钮绑定事件
            $("#updateBtn").click(function (){

                var id = $.trim($("#edit-id").val());
                var owner = $.trim($("#edit-owner").val());
                var name = $.trim($("#edit-name").val());
                var startDate = $.trim($("#edit-startDate").val());
                var endDate = $.trim($("#edit-endDate").val());
                var cost = $.trim($("#edit-cost").val());
                var description = $.trim($("#edit-description").val());

                $.ajax({
                    url : "${pageContext.request.contextPath}/workbench/activity/update.do",
                    data : {
                        "id" : id,
                        "owner" : owner,
                        "name" : name,
                        "startDate" : startDate,
                        "endDate" : endDate,
                        "cost" : cost,
                        "description" : description
                    },
                    type : "post",
                    dataType : "json",
                    success : function (data){
                        if (data.success){
                            // 关闭模态窗口
                            $("#editActivityModal").modal("hide");
                            // pageList(1, 2);
                            // 修改操作后 停留在当前页 维持已经设置好的每页展现的记录数
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        }else {
                            alert("修改失败");
                        }
                    }
                })
            })

            // 点击查询按钮触发pageList方法
            $("#searchBtn").click(function () {

                // 点击查询按钮的时候 把搜索框中的信息保存到隐藏域
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-startDate").val($.trim($("#search-startDate").val()));
                $("#hidden-endDate").val($.trim($("#search-endDate").val()));

                pageList(1, 2);
            })

            // 为全选按钮绑定事件
            $("#qx").click(function (){
                $("input[name=xz]").prop("checked",this.checked);
            })

            // 复选控制全选
            $("#activityBody").on("click", $("input[name=xz]"), function (){
                $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);
            })

            // 为删除按钮添加事件
            $("#deleteBtn").click(function (){

                // 获取选中的jquery对象
                var $xz = $("input[name=xz]:checked");

                if ($("input[name=xz]:checked").length==0){
                    alert("请选择要删除的记录");
                }else{

                    if (confirm("确定删除所选中的记录吗？")){
                        var param = "";
                        for (var i=0; i<$xz.length; i++){
                            param += "id="+$($xz[i]).val();
                            // 如果不是最后一个 追加&
                            if (i < ($xz.length-1)){
                                param += "&";
                            }
                        }
                        //alert(param);
                        $.ajax({
                            url : "${pageContext.request.contextPath}/workbench/activity/delete.do",
                            data : param,
                            type : "post",
                            dataType : "json",
                            success : function (data){
                                // 需要从后台传回success
                                if (data.success){
                                    // pageList(1, 2);
                                    // 删除操作后 返回到第一页(删除后当前页可能不存在 所以返回第一页)
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                }else {
                                    alert("删除失败");
                                }
                            }
                        })
                    }
                }
            })
        });

        function pageList(pageNo, pageSize) {

            // 把全选的√去掉
            $("#qx").prop("checked", false);

            // 查询前 把隐藏域中的信息 传给搜索框
            $("#search-name").val($.trim($("#hidden-name").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-startDate").val($.trim($("#hidden-startDate").val()));
            $("#search-endDate").val($.trim($("#hidden-endDate").val()));



            $.ajax({
                url: "${pageContext.request.contextPath}/workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    // 后台穿回来的是 {”total“:100, "dataList":[{市场活动1}，{2}，{3}]}
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                        // 每个n是一个activity对象
                        html += '<tr class="active">';
                        html += '<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'${pageContext.request.contextPath}/workbench/activity/detail.do?id='+n.id+'\';">' + n.name + '</a></td>';
                        html += '<td>' + n.owner + '</td>';
                        html += '<td>' + n.startDate + '</td>';
                        html += '<td>' + n.endDate + '</td>';
                        html += '</tr>';
                    })
                    $("#activityBody").html(html);

                    // 计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                    //数据处理完毕 结合分页插件对前端进行分页展示
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });
                }
            })
        }

    </script>
</head>
<body>

<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="activityAddForm">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <input type="hidden" id="edit-id">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startDate" >
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endDate">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addbtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn">
                    <span class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx" /></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--<tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;"
                           onclick="window.location.href='detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage"></div>
        </div>
    </div>
</div>
</body>
</html>