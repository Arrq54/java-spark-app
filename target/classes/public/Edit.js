window.onload = ()=>{
    refreshImage();
}
const div = document.getElementById("canvas")
let realHeight, realWidth;
function refreshImage(){
    let name =  new URLSearchParams(window.location.search).get('name');
   
    fetch(`/getwidthheight?name=${name}`, { method: "get" })
    .then(response => response.json())
    .then(data => {
        
        div.style.backgroundImage = `url("/thumb?id=${name}&rand=${Math.random()}")`
        console.log(data);
        realHeight = data.height;
        realWidth = data.width
        if(data.height <=400){
            div.style.width = data.width+"px";
            div.style.height = data.height+"px";    
        }else{
            let ratio = data.width/data.height;
            div.style.height = "400px";
            div.style.width = ratio * 400+"px";
        }
                       
    })
 .catch(error => console.log(error))
}

let startX,startY, drawing;
div.onmousedown = (e)=>{

    div.innerHTML = ""
    startX = e.offsetX;
    startY = e.offsetY;
    drawing = true;
}
div.onmouseup = (e)=>{

    drawing = false;
    let endX = e.offsetX;
    let endY = e.offsetY;
    if(endX > startX && endY> startY){
        drawRect(startX, startY,  endX - startX, endY-startY)
        cropX = startX;
        cropY = startY;
        cropW = endX - startX;
        cropH = endY-startY
    }
    // startX=0;
    // startY=0
}
div.onmousemove = (e)=>{
    if(drawing){
        let endX = e.offsetX;
        let endY = e.offsetY;
        if(endX > startX && endY> startY){
            drawRect(startX, startY,  endX - startX, endY-startY)
        }
        
    }
    
} 

function drawRect(x,y,w,h){
    div.innerHTML = ""
    let rect = document.createElement("div")
    rect.classList.add("selectedRect")
    rect.style.left = `${x}px`;
    rect.style.top = `${y}px`;
    rect.style.width = `${w}px`;
    rect.style.height = `${h}px`;
    div.appendChild(rect);
}

let cropX, cropY, cropW, cropH;

let crop = async () =>{
    div.innerHTML = ""
    if(realHeight > 400){
        cropX = (cropX/((realWidth/realHeight)*400)) * realWidth
        cropY = (cropY/(400)) * realHeight;
        cropW =  (cropW/((realWidth/realHeight)*400))*realWidth
        cropH =  (cropH/(400)) * realHeight
        
    }
    const data = JSON.stringify({
        x: Math.round(cropX),
        y: Math.round(cropY),
        w: Math.round(cropW),
        h: Math.round(cropH),
        filename: new URLSearchParams(window.location.search).get('name')
    })
    const options = {
        method: "POST",
        body: data,
    };
    let response = await fetch("/cropimage", options)
    if (!response.ok)
        return response.status
    else
        refreshImage();
}

let rotate = async () =>{
    const options = {
        method: "POST",
        body: JSON.stringify({filename: new URLSearchParams(window.location.search).get('name')}),
    };
    let response = await fetch("/rotateimg", options)
    if (!response.ok)
        return response.status
    else
        refreshImage();
}

let flipH = async () =>{
    const options = {
        method: "POST",
        body: JSON.stringify({filename: new URLSearchParams(window.location.search).get('name')}),
    };
    let response = await fetch("/flipH", options)
    if (!response.ok)
        return response.status
    else
        refreshImage();
}
let flipV = async () =>{
    const options = {
        method: "POST",
        body: JSON.stringify({filename: new URLSearchParams(window.location.search).get('name')}),
    };
    let response = await fetch("/flipV", options)
    if (!response.ok)
        return response.status
    else
        refreshImage();
}