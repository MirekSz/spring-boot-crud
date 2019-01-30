xml.firstName.replaceNode {
	item(who: 'mrhaki', priority: '500') {
		title 'Download Grails 1.4 M1'
	}
};

xml.children.child.findAll {it.@age!=null}.each {
	it.@age.text().toInteger() >10?it.@miro='true':''
};

println groovy.xml.XmlUtil.serialize( xml )