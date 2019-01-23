	import groovy.xml.*;

			def result='<order>';
			def lines = request.body.getFile().readLines();
			for (def line in lines){
				def code =  line.split(':');
				def cc = code[0].replaceAll("\"","").split("-");
				result+="""<${cc[0]} comment=${code[1]}>${cc[1]}</${cc[0]}>""";
			}

			result+='</order>';
			request.headers.CamelFileName=request.headers.CamelFileName+'.xml';
			exchange.in.body = result;
