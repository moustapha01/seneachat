function init(){
    const fixedHeightElem = document.querySelector("#list-component");
    const rowParentElem = document.querySelector("#list-body");
    const lastRowElem = rowParentElem.querySelector("tr:last-child");
    const fixedHeightElemClientRect = fixedHeightElem.getBoundingClientRect();
    const messagElem = document.querySelector("#message");
    let oldRange = document.getElementById('oldRange').value;
    if (Number.isNaN(oldRange) || oldRange === "") {
        oldRange = 0;
    }
    let newRange = 0;

    fixedHeightElem.addEventListener("scroll", function(){
        const lastRowElemClientRect = lastRowElem.getBoundingClientRect();
        let messTxt, color;
        if (lastRowElemClientRect.bottom > fixedHeightElemClientRect.bottom){
            const diff = lastRowElemClientRect.bottom - fixedHeightElemClientRect.bottom;
        } else if (lastRowElemClientRect.top < fixedHeightElemClientRect.top) {
            messTxt = "Last element went up the viewport and the list blank now. Calculate range by scroll height divided by row height. this is case 3";
            newRange = Math.round(fixedHeightElem.scrollTop / 50);
            messTxt += " New Range is " + (newRange - 10)  + " - " + (newRange + 10);
        } else {
            messTxt = "Last element is in the viewport and there will be some gap in the bottom.  Render next range here. This is case2";
            color = "#85cc94";
        }

        if(oldRange != newRange && Math.abs(newRange - oldRange) >= 5){
            document.getElementById('sellerAdsForm').action = '/web/dashboard';
            document.getElementById('sellerAdsForm').method = 'post';
            document.getElementById('listRange').value = newRange;
            document.getElementById('oldRange').value = newRange;
//            document.sellerAdsForm.submit();
        }
    });
}

function scrollPage() {
    const listRange = Number.parseInt(document.getElementById('listRange').value);
    if(listRange > 0) {
        document.querySelector("#list-component").scrollTop = 10 * listRange;
        console.log("current list range is:", document.getElementById('listRange').value);
    }
}

document.addEventListener("DOMContentLoaded", scrollPage);
document.addEventListener("DOMContentLoaded", init);
