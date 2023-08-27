const customPaging = (MAX_LINK, BASE_URL, pageSize, totalElement, currentPage) => {
    if(currentPage>=pageSize) return null

    const anchorList = []
    if(pageSize < MAX_LINK) MAX_LINK = pageSize

    // disp 시작할 page 결정
    let startPageLink = currentPage - Math.floor(MAX_LINK/2)
    if(startPageLink <0) startPageLink = 0
    else if( (startPageLink + MAX_LINK) > pageSize ) {
        startPageLink=pageSize-MAX_LINK
        if(startPageLink <0 ) startPageLink = 0
    }

    // 시작이 0이 아니면 설정
    if(startPageLink !== 0) {
        const anchorFirst = document.createElement("a");
        anchorFirst.href = BASE_URL + "page=0";
        anchorFirst.textContent = "시작";
        anchorList.push(anchorFirst)
    }

    // 앞에 안 나온 page 있으면 설정
    if( startPageLink > 0){
        const anchorPrev = document.createElement("a");
        let moveTo = currentPage - MAX_LINK
        if(moveTo < 0) moveTo = 0
        anchorPrev.href = BASE_URL + `page=${moveTo}`
        anchorPrev.textContent = "이전";
        anchorList.push(anchorPrev)
    }

    // page link 설정
    for(let i=0; i<MAX_LINK; i++){
        let anchor = document.createElement("a");
        anchor.href = BASE_URL + "page=" + (startPageLink + i);
        anchor.textContent = "" + (startPageLink + i);
        if( (startPageLink + i) === currentPage ){
            anchor.style.fontStyle = "italic"
            anchor.style.fontWeight = "800"
            anchor.style.color = "magenta"
        }
        anchorList.push(anchor)
    }

    //뒤에 안 나온 page 있으면 설정
    if( startPageLink + MAX_LINK < pageSize) {
        const anchorNext = document.createElement("a");
        let moveTo = currentPage + MAX_LINK
        if(moveTo >= pageSize) moveTo=pageSize-1
        anchorNext.href = BASE_URL + `page=${moveTo}`
        anchorNext.textContent = "다음"
        anchorList.push(anchorNext)
    }

    // 끝이  끝이 아니면 설정
    if(startPageLink < pageSize -MAX_LINK){
        const anchorLast = document.createElement("a");
        anchorLast.href = BASE_URL + `page=${pageSize-1}`;
        anchorLast.textContent = "끝";
        anchorList.push(anchorLast)
    }

    return anchorList
}