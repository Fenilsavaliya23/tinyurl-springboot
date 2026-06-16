function copyShortUrl(button) {

    const url =
        document.querySelector(".result a").href;

    navigator.clipboard.writeText(url);

    button.innerText = "Copied ✓";

    button.classList.add("copy-success");

    setTimeout(()=>{
        button.innerText="Copy";
        button.classList.remove("copy-success");
    },2000);

}
