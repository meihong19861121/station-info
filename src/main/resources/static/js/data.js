var selectnode=''
layui.use(['table','laydate','util'], function(){
    var table = layui.table;
    var laydate = layui.laydate;
    var util = layui.util;

    $.ajaxSetup({
        complete: function (XMLHttpRequest, textStatus) {
            if (textStatus=='parsererror') {
                top.location.href ="/login.html";
            }
        }
    });

    //第一个实例
    table.render({
        elem: "#demo"
        ,height: 500
        ,url: "/getInfo" //数据接口
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: "name", title: "姓名",  sort: true, fixed: "left",width:80}
            ,{field: "sex", title: "性别",  sort: true,width:80}
            ,{field: "age", title: "年龄",width:60 }
            ,{field: "idCardNo", title: "身份证号",width:180, sort: true}
            ,{field: "phone", title: "联系电话",width:120}
            ,{field: "temperature", title: "体温", sort: true,width:70}
            ,{field: "reporttime", sort: true,width:150,title: "时间",templet:function(d){return util.toDateString(d.reporttime, "yyyy-MM-dd HH:mm");}}
            ,{field: "stationname", title: "站点名称",width:150}
            ,{field: "comment", title: "备注",width:100}

        ]]
    });

    //执行一个laydate实例
    laydate.render({
        elem: '#startDate', //指定元素
        type: 'datetime'
    });
    laydate.render({

        elem: '#endDate' //指定元素
        ,type: 'datetime'
    });

    $('#chaxun').click(function () {
        var startDate = $('#startDate').val();
        var endDate=$('#endDate').val();
        if(startDate==''&&endDate!=''){
            layer.msg('选择开始时间');
            return
        }
        if(startDate!=''&&endDate==''){
            layer.msg('选择结束时间');
            return
        }
        table.reload('demo', {
            url: '/getInfo'
            // ,methods:"post"
            ,request: {
                pageName: 'page'  //页码的参数名称，默认：page
                ,limitName: 'limit'  //每页数据量的参数名，默认：limit
            }
            ,where: {
                endDate:endDate,
                startDate:startDate,
                selectnode:selectnode
            }
            ,page: {
                curr: 1
            }
        });
    })

    $('#daochu').click(function () {
        jQuery.download = function (url, method, stationId, startDate,endDate) {
            jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' +  // action请求路径及推送方法
                '<input type="text" name="stationId" value="' + selectnode + '"/>' + // 文件路径
                '<input type="text" name="startDate" value="' + startDate + '"/>' + // 文件名称
                '<input type="text" name="endDate" value="' + endDate + '"/>' + // 文件名称
                '</form>')
                .appendTo('body').submit().remove();
        };
        var startDate = $('#startDate').val();
        var endDate=$('#endDate').val();
        if(startDate==''&&endDate!=''){
            layer.msg('选择开始时间');
            return
        }
        if(startDate!=''&&endDate==''){
            layer.msg('选择结束时间');
            return
        }
        jQuery.download("/export", 'post', '', startDate,endDate);
    })

















    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url:"/getNodes",
            autoParam:["id", "name=n", "level=lv"],
            otherParam:{"otherParam":"zTreeAsyncTest"},
            dataFilter: filter
        },
        callback: {
            beforeClick: beforeClick,
            beforeAsync: beforeAsync,
            onAsyncError: onAsyncError,
            onAsyncSuccess: onAsyncSuccess
        }
    };

    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i=0, l=childNodes.length; i<l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
        }
        return childNodes;
    }

    function beforeClick(treeId, treeNode) {
        selectnode=treeNode.id;
        var startDate = $('#startDate').val();
        var endDate=$('#endDate').val();
        if(startDate==''&&endDate!=''){
            layer.msg('选择开始时间');
            return
        }
        if(startDate!=''&&endDate==''){
            layer.msg('选择结束时间');
            return
        }
        table.reload('demo', {
            url: '/getInfo'
            // ,methods:"post"
            ,request: {
                pageName: 'page'  //页码的参数名称，默认：page
                ,limitName: 'limit'  //每页数据量的参数名，默认：limit
            }
            ,where: {
                endDate:endDate,
                startDate:startDate,
                stationId:selectnode
            }
            ,page: {
                curr: 1
            }
        });
        return true;
    }
    var log, className = "dark";
    function beforeAsync(treeId, treeNode) {
        className = (className === "dark" ? "":"dark");
        showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
        return true;
    }
    function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
        showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }
    function onAsyncSuccess(event, treeId, treeNode, msg) {
        showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
    }

    function showLog(str) {
        if (!log) log = $("#log");
        log.append("<li class='"+className+"'>"+str+"</li>");
        if(log.children("li").length > 8) {
            log.get(0).removeChild(log.children("li")[0]);
        }
    }
    function getTime() {
        var now= new Date(),
            h=now.getHours(),
            m=now.getMinutes(),
            s=now.getSeconds(),
            ms=now.getMilliseconds();
        return (h+":"+m+":"+s+ " " +ms);
    }

    function refreshNode(e) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            type = e.data.type,
            silent = e.data.silent,
            nodes = zTree.getSelectedNodes();
        if (nodes.length == 0) {
            alert("请先选择一个父节点");
        }
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.reAsyncChildNodes(nodes[i], type, silent);
            if (!silent) zTree.selectNode(nodes[i]);
        }
    }

    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting);
        $("#refreshNode").bind("click", {type:"refresh", silent:false}, refreshNode);
        $("#refreshNodeSilent").bind("click", {type:"refresh", silent:true}, refreshNode);
        $("#addNode").bind("click", {type:"add", silent:false}, refreshNode);
        $("#addNodeSilent").bind("click", {type:"add", silent:true}, refreshNode);
    });
});







