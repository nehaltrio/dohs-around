const el = document.getElementById("show");

const hiddenDiv = document.getElementById("hide");

el.addEventListener("mouseover", function handleMouseOver() {
  hiddenDiv.style.visibility = "visible";
});

el.addEventListener("mouseout", function handleMouseOut() {
  hiddenDiv.style.visibility = "hidden";
});

hiddenDiv.addEventListener("mouseover", function handleMouseOver() {
  hiddenDiv.style.visibility = "visible";
});

hiddenDiv.addEventListener("mouseout", function handleMouseOut() {
  hiddenDiv.style.visibility = "hidden";
});

const mainCat = document.getElementById("main1");
const subCat = document.getElementById("show1");

const mainCat2 = document.getElementById("main2");
const subCat2 = document.getElementById("show2");

mainCat.addEventListener("mouseover", function handleMouseOver() {
  subCat.style.visibility = "visible";
});

mainCat.addEventListener("mouseout", function handleMouseOut() {
  subCat.style.visibility = "hidden";
});

subCat.addEventListener("mouseover", function handleMouseOver() {
  subCat.style.visibility = "visible";
});

subCat.addEventListener("mouseout", function handleMouseOut() {
  subCat.style.visibility = "hidden";
});

mainCat2.addEventListener("mouseover", function handleMouseOver() {
  subCat2.style.visibility = "visible";
});

mainCat2.addEventListener("mouseout", function handleMouseOut() {
  subCat2.style.visibility = "hidden";
});

subCat2.addEventListener("mouseover", function handleMouseOver() {
  subCat2.style.visibility = "visible";
});

subCat2.addEventListener("mouseout", function handleMouseOut() {
  subCat2.style.visibility = "hidden";
});

$(function(){

  $("#show_search_cat")
  .focusin(function () {
    $("#cat_search").show();
  })
  .focusout(function () {
    $("#cat_search").hide();
  });

});





