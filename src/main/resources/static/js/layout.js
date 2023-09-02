const listCharSpan = document.querySelectorAll("#titleDiv > div");
//console.log(listCharSpan)
let curEnlargedSpan = 0

const timer = setInterval( ()=> {
    console.log("timer")

    for(i=0; i<listCharSpan.length; i++){
        if(i === curEnlargedSpan) {
            listCharSpan[i].classList.add("enlargedCharSpan")
            listCharSpan[i].classList.remove("normalCharSpan")
        } else {
            listCharSpan[i].classList.add("normalCharSpan")
            listCharSpan[i].classList.remove("enlargedCharSpan")
        }
    }

    curEnlargedSpan++
    if(curEnlargedSpan===8) curEnlargedSpan=0

}, 1000)
