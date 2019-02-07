/**
 * 基于echarts3.7.2版本（3.0版本和2.0版本有差异）
 * 自定义主题（自定义主题可在echarts官网的 下载-->主题下载-->自定义主题 处导出）
 */
/**
 * 定义统计图通用常量
 */
var COLOR_ARRAY = [
                "#2a9aff",
                "#6cba5f",
                "#c766ff",
                "#2fbc93",
                "#5bd5e7",
                "#5ffea2",
                "#ffe866",
                "#0071d6",
                "#fe5f5f",
                "#712fbc"];
var TITLE_TEXT_STYLE = {fontSize: 16, fontWeight: 'normal', color: '#000000'};

/**
 * Echarts自适应屏幕
 * @param chart
 */
/*function adaptionWindows(chart) {
    window.addEventListener('resize',function () {
        chart.resize();
    });
}*/

/**
 * 创建饼图
 * @param chart echarts图表对象
 * @param titleText 主标题内容
 * @param legendArray 图例数据数组
 * @param seriesArray 系列数据数组
 */
function createPieChart(chart, titleText, legendArray, seriesArray){
    var option = {
        title: { text: titleText, textStyle: TITLE_TEXT_STYLE },
        legend: { data: legendArray, left: 'center', top: 'bottom' },
        //tooltip: { formatter: "{a} <br/>{b}:{c} ({d}%)" },
        tooltip: { formatter: function (params) {
            // 保留两位小数
            return params.seriesName + ' <br/>' + params.name + ':' + params.value + ' (' +(params.percent - 0).toFixed(2) + '%)'
            }
        },
        toolbox: {
            show: true,
            itemSize: 15,
            itemGap: 15,
            left: 'right',
            top: 'top',
            feature: {
                //saveAsImage: {show: true},
                dataView: {show: true},
                restore: {show: true}
            }
        },
        series: [{
            name: titleText,
            type: 'pie',
            label: { normal: { show: true,
                formatter: function (params) {
                    // 保留两位小数
                    return params.value + ' (' +(params.percent - 0).toFixed(2) + '%)'
                }
            },
                emphasis: { show: true, formatter: "{c} ({d}%)" }
            },
            labelLine: { normal: { show: true },
                emphasis: { show: true }
            },
            center: ['50%', '50%'],
            radius: [0, '50%'],
            data: seriesArray
        }],
        color: COLOR_ARRAY,
        animation: false
    };

    chart.setOption(option);
    $(window).resize(function () {
        chart.resize(option);
    });
    return chart;
}

/**
 * 创建柱状图(非堆积柱状图)
 */
function createBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray){
    return createBarOrLineChart('bar', chart, titleText, legendArray, xAxisType, categoryArray, valueArray)
}

/**
 * 创建折线图
 */
function createLineChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray){
    return createBarOrLineChart('line', chart, titleText, legendArray, xAxisType, categoryArray, valueArray)
}

/**
 * 创建柱状图(非堆积柱状图)或折线图
 * @param chartType 图表类型（目前可选bar或line）
 * @param chart echarts图表对象
 * @param titleText 主标题内容
 * @param legendArray 图例数据数组(和系列名称数组，这两个为同一组数据)
 * @param xAxisType xAxis的type的类别，暂时可选category或者value, 一般选category
 * @param categoryArray 类别数据数组（一般为xAxis的数据）
 * @param valueArray 系列数据数组（数据示例：[[{},{}], [{},{}]]）
 */
function createBarOrLineChart(chartType, chart, titleText, legendArray, xAxisType, categoryArray, valueArray){
    // 准备series数据
    var seriesInfo = [];
    if(legendArray.length > 0) {
        $.each(legendArray, function(index, seriesName) {
            var seriesItem = {
                name:seriesName,
                type:chartType,
                data:valueArray[index],
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'top',
                            formatter:function(params){
                                if(params.value === 0){
                                    return '';
                                } else {
                                    return params.value;
                                }
                            }
                        }
                    }
                },
                barGap: '1%'
            };
            seriesInfo.push(seriesItem);
        });
    }

    // 根据xAxisType判断xAxis和yAxis的类型和数据
    var xAxisInfo = [];
    var yAxisInfo = [];
    if(xAxisType === "value") {
        xAxisInfo.push({type: 'value'});
        yAxisInfo.push({type: 'category', data : categoryArray});
    } else {
        xAxisInfo.push({type: 'category', data : categoryArray});
        yAxisInfo.push({type: 'value'});
    }

    var option = {
        title: { text: titleText, textStyle: TITLE_TEXT_STYLE },
        legend: { data: legendArray, left: 'center', top: 'bottom' },
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
        },
        toolbox: {
            show: true,
            itemSize: 15,
            itemGap: 15,
            left: 'right',
            top: 'top',
            feature: {
                magicType: {show: true, type: ['line', 'bar'] },
                //saveAsImage: {show: true},
                dataView: {show: true},
                restore: {show: true}
            }
        },
        xAxis: xAxisInfo,
        yAxis: yAxisInfo,
        series: seriesInfo,
        color: COLOR_ARRAY,
        animation: false
    };

    chart.setOption(option);
    $(window).resize(function () {
        chart.resize(option);
    });
    return chart;
}

/**
 * 创建堆积柱状图（单条柱子堆积）
 * @param chart echarts图表对象
 * @param titleText 主标题内容
 * @param legendArray 图例数据数组(和系列名称数组，这两个为同一组数据)
 * @param xAxisType xAxis的type的类别，暂时可选category或者value, 一般选category
 * @param categoryArray 类别数据数组（一般为xAxis的数据）
 * @param valueArray 系列数据数组（数据示例：[[{},{}], [{},{}]]）
 */
function createStackBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray){
    chart = createBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray);

    // 准备新的series数据
    var seriesInfo = [];
    if(legendArray.length > 0) {
        $.each(legendArray, function(index, seriesName) {
            var seriesItem = {
                name:seriesName,
                type:'bar',
                data:valueArray[index],
                stack: titleText,
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'inside',
                            formatter:function(params){
                                if(params.value === 0){
                                    return '';
                                } else {
                                    return params.value;
                                }
                            }
                        }
                    }
                }
            };
            seriesInfo.push(seriesItem);
        });
    }

    chart.setOption({
        series: seriesInfo
    });
    return chart;
}



function getImgData(chart){
    return chart.getDataURL({type: 'png',
        pixelRatio: 2,
        backgroundColor: '#FFFFFF',
        excludeComponents: ['toolbox']});
}

function pieChartTest() {
    var chart = echarts.init(document.getElementById("pieDemo"));
    chart.showLoading(); // 3.0版本目前只有default一种loading样式
/*
    // 3.0版本异步加载数据
    $.get('data.json').done(function (data) {
        // 填入数据
        myChart.setOption({
            xAxis: {
                data: data.categories
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '销量',
                data: data.data
            }]
        });
    });*/

    var titleText = "某站点用户访问来源";
    var legendArray = ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'];
    var seriesArray = [
        {value:335, name:'直接访问'},
        {value:310, name:'邮件营销'},
        {value:234, name:'联盟广告'},
        {value:135, name:'视频广告'},
        {value:1548, name:'搜索引擎'}];
    chart = createPieChart(chart, titleText, legendArray, seriesArray);
    // 根据需要修改默认样式
    /*chart.setOption({
        toolbox: { show: false}
    });*/
    chart.hideLoading();

    // 导出的图片分辨率比例，默认为 1。
    return getImgData(chart);
}

function barChartTest(){
    var chart = echarts.init(document.getElementById("barDemo"));
    chart.showLoading(); // 3.0版本目前只有default一种loading样式

    var titleText = "某地区蒸发量和降水量";
    var legendArray = ['蒸发量','降水量'];
    var xAxisType = 'category';
    var categoryArray = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
    var valueArray = [[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
        [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
    ];
    chart = createBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray);
    // 根据需要修改默认样式
    /*chart.setOption({
        toolbox: { show: false}
    });*/
    chart.hideLoading();

    // 导出的图片分辨率比例，默认为 1。
    return getImgData(chart);
}

function lineChartTest(){
    var chart = echarts.init(document.getElementById("lineDemo"));
    chart.showLoading(); // 3.0版本目前只有default一种loading样式

    var titleText = "某地区蒸发量和降水量";
    var legendArray = ['蒸发量'];
    var xAxisType = 'category';
    var categoryArray = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
    var valueArray = [[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]];
    chart = createLineChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray);
    // 根据需要修改默认样式
    /*chart.setOption({
        toolbox: { show: false}
    });*/
    chart.hideLoading();

    // 导出的图片分辨率比例，默认为 1。
    return getImgData(chart);
}
function barChartTest2(){
    var chart = echarts.init(document.getElementById("barDemo2"));
    chart.showLoading(); // 3.0版本目前只有default一种loading样式
    var titleText = "某地区蒸发量和降水量";
    var legendArray = ['蒸发量'];
    var xAxisType = 'category';
    var categoryArray = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
    var valueArray = [[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]];

    chart = createBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray);
    chart.setOption({
        legend: { show: false }
    });
    chart.hideLoading();
    // 导出的图片分辨率比例，默认为 1。
    return getImgData(chart);
}

function stackBarChartTest(){
    var chart = echarts.init(document.getElementById("stackBarDemo"));
    chart.showLoading(); // 3.0版本目前只有default一种loading样式
    var titleText = "某站点用户访问来源";
    var legendArray = ['直接访问', '邮件营销','联盟广告','视频广告','搜索引擎'];
    var xAxisType = 'value';
    var categoryArray = ['周一','周二','周三','周四','周五','周六','周日'];
    var valueArray = [[320, 302, 301, 334, 390, 330, 320],
                      [120, 132, 101, 134, 90, 230, 210],
                      [220, 182, 191, 234, 290, 330, 310],
                      [150, 212, 201, 154, 190, 330, 410],
                      [820, 832, 901, 934, 1290, 1330, 1320]
    ];
    chart = createStackBarChart(chart, titleText, legendArray, xAxisType, categoryArray, valueArray);
    /*chart.setOption({
        toolbox: { show: false}
    });*/
    chart.hideLoading();
    // 导出的图片分辨率比例，默认为 1。
    return getImgData(chart);
}





