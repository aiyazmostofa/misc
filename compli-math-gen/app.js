function generateWorksheet(n) {
    let html = "";
    let expressions = [];
    for (let i = 0; i < n; i++) expressions.push(generateExpression());
    for (let i = 0; i < n; i++) {
        html += `
        <div class="problem">
            <div class="equation">
                \\(${
            i + 1 + ".\\hspace{1em}" + expressions[i].latex
        }\\)
            </div>
            <div class="rule"></div>
        </div>
    ` + "\n";
    }

    function formatAnswer(i) {
        let answer = expressions[i].value;
        answer = answer.toExponential("2");
        answer = answer.replace(/e/, "\\times 10^{") + "}";
        return answer.replace(/\+/, "");
    }

    html += '<div class="page-break"></div>\n<div class="columns">\n';
    let i = 0;
    let bound = 20;
    while (i < n) {
        html += '<div>\n';
        for (; i < bound && i < n; i++) {
            html += `
            <div class="answer">
                \\(${i + 1 + ".\\hspace{1em}" + formatAnswer(i)}\\)
            </div>
        ` + "\n";
        }
        bound += 20;
        html += '</div>\n<div>\n';
        for (; i < bound && i < n; i++) {
            html += `
            <div class="answer">
                \\(${i + 1 + ".\\hspace{1em}" + formatAnswer(i)}\\)
            </div>
        ` + "\n";
        }
        bound += 20;
        html += '</div>\n'
        if (i < n) {
            html += '</div>\n<div class="page-break"></div>\n<div class="columns">\n';
        } else {
            html += '</div>'
        }
    }
    return html;
}

function injectWorksheet(id, n) {
    if (n > 600) n = 600;
    if (n < 15) n = 15;
    document.getElementById(id).innerHTML = generateWorksheet(n);
    document.getElementById(id).innerHTML = document
        .getElementById(id)
        .innerHTML.replace(/undefined/g, "");
}

function printWorksheet() {
    MathJax.typesetPromise()
        .then(() => injectWorksheet("print", document.getElementById("quantity").value))
        .then(() => MathJax.typesetPromise())
        .then(() => print());
}