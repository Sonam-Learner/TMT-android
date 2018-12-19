 var data;
 var xhttp = new XMLHttpRequest();
 xhttp.onreadystatechange=function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
      // console.log(xhttp.responseText);
      data=JSON.parse(xhttp.responseText);
      myCatalog();
    }
  };
  xhttp.open("GET", "/static/catalog.json", true);
  xhttp.send();


var count=0;
 function myCatalog()
  {
  	if (count==0) {
  		document.getElementById('Btn1').disabled=true;
  	}
  	var catalog=data.items;
  	// console.log(catalog.length);
  	var mytitle="";
  	var mydescript="";
  	var myimage="";
  	var mypublisher="";
  	var myyear="";
  	var myauthor="";
  	var myprice="";
  	var mypreview="";
  	var price;
  	var c=catalog[count].volumeInfo;
  	var a=c.authors;
	var s=catalog[count].saleInfo;
  	// console.log(s);
  	if(s["retailPrice"]==undefined){
  		var price1=s.saleability;
  		document.getElementById('price').innerHTML=price1;
  		// document.getElementById('cart').disabled=true;
  	}
  	else{
  		price=s.retailPrice;
  		myprice+=price.currencyCode+". "+price.amount;
  		document.getElementById('price').innerHTML=myprice;
  		// document.getElementById('cart').disabled=false;
  	}
  		
  	for (var j = 0; j < a.length; j++) {
  		myauthor+=a[j]+" ";
  	}
  	mytitle+="Title: "+c.title;
  	mydescript+=c.description;
  	myimage+=(c.imageLinks).thumbnail;
  	mypreview+=c.previewLink;
  	mypublisher+=c.publisher;
  	myyear+=c.publishedDate;
  	// console.log(price);

  	
  	document.getElementById('title').innerHTML=mytitle;
  	document.getElementById('describe').innerHTML=mydescript;
  	document.getElementById('image').src=myimage;
  	document.getElementById('publisher').innerHTML=mypublisher;
  	document.getElementById('year').innerHTML=myyear;
  	document.getElementById('author').innerHTML=myauthor;
  	document.getElementById('anchor').href=mypreview;
  }

function myNext() {
	count+=1;
	var catalog=data.items;
	myCatalog();
	if (count==(catalog.length)-1) {
		document.getElementById("Btn2").disabled = true;
		document.getElementById("Btn1").disabled = false;
	}
	else{
		document.getElementById("Btn1").disabled = false;	
	} 
}
function myPrevious() {
	count-=1;
	myCatalog();
	if (count==0) {
		document.getElementById("Btn1").disabled = true;
		document.getElementById("Btn2").disabled = false;

	}
	else{
		document.getElementById("Btn2").disabled = false;
	}
}

function myCart(){
	var catalog=data.items;
	var B_id=catalog[count].id;
	var volume=catalog[count].volumeInfo;
	var B_title=volume.title;
	var s=catalog[count].saleInfo;
	var sprice=s.retailPrice;
	var B_price=sprice.amount;
	xhttp.open("GET", '/'+B_id+'/'+B_title+'/'+B_price,true);
    xhttp.send();	
    // console.log(B_title);
}
