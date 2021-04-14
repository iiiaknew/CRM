<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
        $(function (){
            // 如果当前页不是顶级窗口 就设置为顶级窗口
            if (window.top!=window){
                window.top.location = window.location;
            }

            // 页面加载完毕用户名文本框自动清空
            $("#loginAct").val("");

            // 页面加载完毕 用户名获得焦点
            $("#loginAct").focus();

            // 监听键盘回车键
            $(window).keydown(function (event){
                if (event.keyCode == 13){
                    login();
                }
            })

            // 按钮登录事件
            $("#loginbtn").click(function (){
                login();
            })
        })

        function login(){
            //alert("登录操作")
            var loginAct = $.trim($("#loginAct").val());
            var loginPwd = $.trim($("#loginPwd").val());
            if (loginAct == "" || loginPwd == ""){
                $("#msg").html("账号密码不能为空");
                // 如果账号或密码为空 需要强制终止方法
                return false;
            }

            $.ajax({
                url : "settings/user/login.do",
                data : {
                    "loginAct":loginAct,
                    "loginPwd":loginPwd
                },
                type : "post",
                dataType : "json",
                success : function (data){
                    if (data.success){
                        window.location.href = "${pageContext.request.contextPath}/workbench/index.jsp";
                    }else {
                        $("#msg").html(data.msg);
                    }
                }
            })
        }
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: #ffffff; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index2.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input id="loginAct" class="form-control" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input id="loginPwd" class="form-control" type="password" placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red"></span>

                </div>
                <%--表单中的按钮必须声明为button 不然默认是submit--%>
                <button type="button" id="loginbtn" class="btn btn-primary btn-lg btn-block" style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>