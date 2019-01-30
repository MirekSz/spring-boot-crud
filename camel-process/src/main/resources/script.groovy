groovy.util.slurpersupport.Attributes.metaClass.int = {->return delegate.text().toInteger()}
String.metaClass.int = {->return delegate.toInteger()}

println "2312".int()
xml.firstName.replaceNode {
	item(who: 'mrhaki', priority: '500') {
		title 'Download Grails 1.4 M1'
	}
};

xml.children.child.findAll {it.@age!=null}.each {
	it.@age.int() >10?it.@miro='true':''
};

println groovy.xml.XmlUtil.serialize( xml )