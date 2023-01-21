let div = document.querySelector("#upload")
let infoh3 = document.querySelector("#info")
document.querySelector("#upload").ondragenter = function (e) {

    infoh3.innerHTML = "UPUSC"


    e.stopPropagation();
    e.preventDefault();
}
document.querySelector("html").ondragover = function (e) {
    console.log("dragover nad dokumentem html")
    e.preventDefault(); // usuwa domyślne zachowanie strony po wykonaniu zdarzenia, warto zakomentować i sprawdzić
    e.stopPropagation(); // zatrzymuje dalszą propagację zdarzenia, warto zakomentować i sprawdzić
}


document.querySelector("#upload").ondrop = function (e) {

    console.log("drop na divie")
    e.stopPropagation();
    e.preventDefault();
    const files = e.dataTransfer.files;
    const fd = new FormData();
   for (let i = 0; i < files.length; i++) {
      fd.append(`file${i}`, files[i]);
   }

   console.log(fd)

   // teraz fetch

   
   const body = fd

   fetch("/uploadfiles", { method: "post", body })
         .then(response => response.json())
         .then(data => {
            generatePhotos(data)                     
         })
         .catch(error => console.log(error)) // ew błąd
   

}

let saveFiles = ()=>{
    fetch("/savecurrentfiles", { method: "post", body:{} })
}
function generatePhotos(arr){
    document.getElementById("photos").innerHTML = ""
    for(let i=0;i<arr.length;i++){
        let remove = null;
        let photodiv = document.createElement("DIV");
        photodiv.classList.add("uploadphotodiv")
        remove = document.createElement("DIV")
        remove.innerHTML = "X";
        remove.classList.add("removebutton");

        remove.onclick = ()=>{
            console.log(arr[i]);
            fetch(`/deletefile?name=${arr[i]}`, { method: "get" })
            .then(response => response.json())
            .then(data => {
                generatePhotos(data)                     
            })
         .catch(error => console.log(error))
        }

        photodiv.appendChild(remove);
        let img = document.createElement("img");
        img.src = `thumb?id=${arr[i]}`
        photodiv.appendChild(img)
        document.getElementById("photos").appendChild(photodiv)
    }

}
function generateRandomFileName(){
    const today = new Date(Date.now());
    let filename = today.toISOString();
    filename += String(Math.random() * 1000000 - 0);
    return filename;
}


