<!DOCTYPE html>
<HTML>
	<HEAD>
		<TITLE> ZTREE DEMO - big data common</TITLE>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-store, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
		<META HTTP-EQUIV="expires" CONTENT="0">

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" >
		<meta content="telephone=no" name="format-detection" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<link rel="apple-touch-icon-precomposed" href="icon.png" />
		<link rel="stylesheet" href="<@spring.url ''/>/css/main.css" media="screen">
		<link rel="stylesheet" href="<@spring.url ''/>/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="<@spring.url ''/>/plugins/jquery.js"></script>
		<script type="text/javascript" src="<@spring.url ''/>/plugins/ztree/js/jquery.ztree.core-3.5.min.js"></script>
		<script type="text/javascript" src="<@spring.url ''/>/plugins/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script type="text/javascript" src="<@spring.url ''/>/plugins/ztree/js/jquery.ztree.exhide-3.5.min.js"></script>
		<style>
			@import "<@spring.url ''/>/js/dojox/grid/enhanced/resources/Pagination.css";
			#dataRecordGrid .dojoxGridCell {
				font-size: 12px;
			}
			#reportGrid .dojoxGridCell {
				font-size: 12px;
			}
			#chartLegend {
				font-size: 12px;
			}
		</style>
		<script>
			dojoConfig = {
			/*
				has : {
					"dojo-firebug" : true,
					"dojo-debug-messages" : true
				},
				isDebug : true,
				*/
				deps : ["dijit/Dialog", "dojo/parser", "dbootstrap"],
				parseOnLoad : true,
				foo : "bar",
				async : true,
				packages : [{
					name : "com",
					location : "<@spring.url ''/>/js/com"
				}, {
					location : "<@spring.url ''/>/js/dbootstrap",
					name : 'dbootstrap'
				}]
			};
		</script>
		
		<script type="text/javascript" src="<@spring.url ''/>/js/dojo/dojo.js"></script>
		<script>
			var basePath = "<@spring.url '/'/>";
			require(["com/endigi/dbsystem/main", "dojo/ready"], function(main, ready) {
				ready(function() {
					com.endigi.dbsystem.main.init();
				})
			});
			
			require(["dgrid/Grid","dgrid/OnDemandGrid", "dgrid/Keyboard", "dgrid/Selection", "dgrid/extensions/Pagination", "dgrid/extensions/ColumnHider", "dgrid/extensions/ColumnResizer","dgrid/ColumnSet", "dojo/_base/declare", "dojo/domReady!"], function(Grid,OnDemandGrid, Keyboard, Selection, Pagination, ColumnHider, ColumnResizer,ColumnSet, declare) {
				window.dgrid = {
					CustomGrid : declare([Grid, Keyboard, Pagination, ColumnHider, ColumnResizer, Selection, declare(null, {
						getSelected : function() {
							var results = [];
							for (var id in this.selection) {
								results.push(this.store.get(id));
							}
							return results;
						}
					})]),
					ReportGrid : declare([OnDemandGrid, ColumnResizer, Selection,ColumnSet])
				};
			});
		</script>
	</HEAD>

	<BODY  class="dbootstrap" style="background-color: #f5f5f5;">
		<div id="appLayout" class="demoLayout" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="design: 'headline',gutters:false">
			<div class="edgePanel header" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region: 'top'">
				<div class="row">
					<div class="span3">
						<h1 class="logo"><a href="https://gathercontent.com/"><img src="images/logo.png" alt="Gather Content"></a></h1>
					</div>
					<div class="menu span9 pull-right">
						<ul class="menu-list">
							<li class="menu-item" id="logout-item" style="display: none">
								<button id="menu-logout" data-dojo-type="dijit/form/Button" data-dojo-props='class:"primary"' onclick="com.endigi.dbsystem.main.logout();">
									注销
								</button>
							</li>
							<li class="menu-item" id="register-item">
								<button id="menu-register" data-dojo-type="dijit/form/Button" data-dojo-props='class:"primary"' onclick="com.endigi.dbsystem.dijit.dialog.RegisterDlg.prototype.statics.getInstance().show()">
									注册
								</button>
							</li>
							<li class="menu-item" id="login-item">
								<button id="menu-login" data-dojo-type="dijit/form/Button" data-dojo-props='class:"inverse"' onclick="com.endigi.dbsystem.dijit.dialog.LoginDlg.prototype.statics.getInstance().show()">
									登录
								</button>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div id="leftPanel" class="edgePanel white-panel" data-dojo-type="dojox/layout/ExpandoPane" data-dojo-props="region: 'left', splitter: false,title:'数据库列表',style:'width:200px;overflow: hidden;'">
				<div id="DBSelector" data-dojo-type="com/endigi/dbsystem/dijit/DBSelector"></div>
			</div>
			<div id="mainStackContainer" class="white-panel" data-dojo-type="dijit/layout/StackContainer"   data-dojo-props="region: 'center'">
				<div id="welcomeStack" class="centerPanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="">

				</div>
				<div id="dbIntroStack" class="centerPanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="">
					<div id="dbIntroWrapper"></div>
				</div>
				<div id="indexSelectStack" class="centerPanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="">
					<div class="edgePanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="region: 'left', splitter: false,style:'width:33.3%;'">
						<div class="edgePanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region: 'top', splitter: false,style:'height:50%;background-color:rgb(183, 223, 224)'">
							<div id="LocationSelector" data-dojo-type="com/endigi/dbsystem/dijit/DBLocationSelector"></div>
						</div>
						<div class="edgePanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region: 'center', splitter: false,style:'height:50%;background-color:rgb(183, 223, 224)'">
							<div id="IndexSelector" data-dojo-type="com/endigi/dbsystem/dijit/DBIndexSelector"></div>
						</div>
					</div>
					<div id="IndexFilter" data-dojo-type="com/endigi/dbsystem/dijit/IndexFilter"></div>
					<div class="edgePanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="region: 'right', splitter: false,style:'width:33.3%;'">
						<div class="edgePanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region: 'top', splitter: false,style:'height:35px;background-color:rgb(246, 242, 206)'">
							<div style="float:left">
								<button data-dojo-type="dijit/form/Button" type="button"
								data-dojo-attach-point="_clearBtn">
									清空
								</button>
							</div>
							<div style="float:left">
								<span> 开始时间: </span>
								<input type="text"  value="2005-01-01"
								style="width:100px;" id="queryStartDate" data-dojo-type="dijit/form/DateTextBox"/>
							</div>
							<div style="float:left">
								<span> 结束时间: </span>
								<input type="text" name="expireTime" value="2013-03-01"
								style="width:100px;" id="queryEndDate" data-dojo-type="dijit/form/DateTextBox" />
							</div>
							<div style="float:left">
								<button data-dojo-type="dijit/form/Button" type="button" onclick="com.endigi.dbsystem.main.showReportPanel()">
									生成
								</button>
							</div>
						</div>
						<div class="edgePanel" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="region: 'center', splitter: false">
							<div id="LocationSelected" data-dojo-type="com/endigi/dbsystem/dijit/DBLocationSelected"></div>
							<div id="IndexSelected" data-dojo-type="com/endigi/dbsystem/dijit/DBIndexSelected"></div>
						</div>
					</div>
				</div>
				<div id="reportStack"  data-dojo-type="com/endigi/dbsystem/dijit/ReportPanel" ></div>
			</div>
		</div>
	</BODY>
</HTML>