<%--
  Created by IntelliJ IDEA.
  User: w'j'j
  Date: 2021/5/2
  Time: 19:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="${pageContext.request.contextPath}/ECharts/echarts.min.js"></script>
<script src="${pageContext.request.contextPath}/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript">

    $(function (){
        // 页面加载完毕 绘制统计图表
        getCharts();
    })

    function getCharts(){

        $.ajax({
            url : "${pageContext.request.contextPath}/workbench/transaction/getCharts.do",
            type : "get",
            dataType : "json",
            success : function (data){
                // data : {total:? , dataList : [ {name : ?, value : ?} ]    }
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '交易漏斗图',
                        subtext: '统计交易阶段数量漏斗图'
                    },

                    series: [
                        {
                            name:'漏斗图',
                            type:'funnel',
                            left: '10%',
                            top: 60,
                            //x2: 80,
                            bottom: 60,
                            width: '80%',
                            // height: {totalHeight} - y - y2,
                            min: 0,
                            max: data.total,
                            minSize: '0%',
                            maxSize: '100%',
                            sort: 'descending',
                            gap: 2,
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            labelLine: {
                                length: 10,
                                lineStyle: {
                                    width: 1,
                                    type: 'solid'
                                }
                            },
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1
                            },
                            emphasis: {
                                label: {
                                    fontSize: 20
                                }
                            },
                            data: data.dataList
                            /*[
                                {value: 60, name: '访问'},
                                {value: 40, name: '咨询'},
                                {value: 2, name: '订单'},
                                {value: 800, name: '点击'},
                                {value: 100, name: '展现'}
                            ]*/
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        })


    }
</script>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
