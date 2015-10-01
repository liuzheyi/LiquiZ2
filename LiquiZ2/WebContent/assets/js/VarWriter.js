/*
* Author: Stephen Oro
* Makes a "Clickable Image" that will supply its answer to the quiz.
*
* Overall: The Clickable Image must
* 
*  
* Solution(s):
* 
*/


/*
 * interface: 
 * 
 */

function VarWriter(innerHTML, className, id, width, height){
	var div = Util.div(className, id);
	div.style.height = "100px";
	div.style.width = "600px";
	div.style.backgroundColor = "#ffffff";
	div.style.display = "inline-block";
	div.contentEditable = "true";
	div.style.textAlign = "left";
	div.style.fontFamily = "monospace";
	this.div = div;
	div.addEventListener("keydown",(this.keyDown).bind(this));
	div.valueOf = this.valueOf;
	this.index = 0;
	return div;
}
VarWriter.prototype.appendVar = function(match){
	var tag = Util.span("$"+match.replace("$","")+"$","writervariable");
	tag.contentEditable = false;
	return tag;
};
VarWriter.prototype.tagMatch = function(node,index){
	var text = node.textContent;
	for(var i = index-1; i >= 0; i--){
		var code = text.charCodeAt(i);
		if(code == 160 || code == 32){
			return false;
		}else if(code == 36){
			var match = text.substring(i,index);
			if(match.replace("$","").length <= 0){
				return false;
			}
			this.index = i;
			node.textContent = text.substring(0,i)+text.substring(index,text.length);
			return match;
		}
	}
	return false;
};
VarWriter.prototype.valueOf = function(){
	return this.innerHTML;
};
VarWriter.prototype.keyDown = function(e){
	var key = e.which;
	var shift = e.shiftKey;
	if(key == 52 && shift){
		if(window.getSelection){
			var sel = window.getSelection();
			var node = sel.anchorNode;
			var range = sel.getRangeAt(0);
			var startIndex = range.startOffset;
			if(!range.collapsed){
				var match = range.cloneContents().textContent;
				match = match.replace("$","");
				if(match.length > 0){
					range.deleteContents();
					var tag = this.appendVar(match);
					range.insertNode(tag);
					range.selectNode(tag);
					sel.removeAllRanges();
					sel.addRange(range);
					sel.collapseToEnd();
					e.preventDefault();
					e.stopPropagation();
					if(this.div.oninput)
						this.div.oninput();
				}
			}else{
				var match = this.tagMatch(node,startIndex);
				if(match){
					var tag = this.appendVar(match);
					range.setStart(node,this.index);
					range.setEnd(node,this.index);
					range.insertNode(tag);
					range.selectNode(tag);
					sel.removeAllRanges();
					sel.addRange(range);
					sel.collapseToEnd();
					e.preventDefault();
					e.stopPropagation();
					if(this.div.oninput)
						this.div.oninput();
				}
			}
		}
	}
};

