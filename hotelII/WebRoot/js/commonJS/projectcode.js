var projectcodes=[{name:'0027',value:'去哪儿网'},{name:'0034',value:'酷讯网'},{name:'0108',value:'游比比网'},{name:'0120',value:'铁友网'}];

function getProjectName(projectCode){
	projectCode=$.trim(projectCode).substring(0,4);
	var projectName = null;
		$.each(projectcodes, function(i, n){
				if(n.name == projectCode)  projectName = n.value;
		});
	return projectName;
}