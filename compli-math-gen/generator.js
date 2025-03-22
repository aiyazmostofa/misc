const Operation = {
    NUMBER: 0,
    EULER: 1,
    PI: 2,
    SCIENTIFIC: 3,
    ADDITION: 4,
    SUBTRACTION: 5,
    MULTIPLICATION: 6,
    DIVISION: 7,
    EXPONENTIATION: 8,
    FRACTION: 9,
    SQUARE: 10,
    SQUARE_ROOT: 11,
    NATURAL_LOG: 12,
    LOG_10: 13,
    SIN: 14,
    COS: 15,
    TAN: 16,
    ARCSIN: 17,
    ARCCOS: 18,
    ARCTAN: 19
};

function generateNumberExpression() {
    let value = Math.random() * 10;
    if (Math.random() < 0.5)
        value = -value;
    let exp = Math.floor(Math.random() * 7) - 3;
    let precision;
    if (Math.random() < 0.8)
        precision = 3;
    else if (Math.random() < 0.5)
        precision = 2;
    else
        precision = 1;
    value = parseFloat((value * Math.pow(10, exp)).toPrecision(precision));
    return {
        value: value,
        latex: value.toString(),
        operation: Operation.NUMBER
    };
}

function generateEulerExpression() {
    return {value: Math.E, latex: "e", operation: Operation.EULER};
}

function generatePiExpression() {
    return {value: Math.PI, latex: "\\pi", operation: Operation.PI};
}

function generateScientificExpression() {
    let coefficient = Math.random() * 10;
    if (Math.random() < 0.5)
        coefficient = -coefficient;
    let exponent = Math.floor(Math.random() * 6) - 3;
    if (exponent >= 0) exponent++;
    if (Math.random() < 0.5)
        exponent = -exponent;
    let value = parseFloat((coefficient * Math.pow(10, exponent)).toPrecision(3));
    return {
        value: value,
        latex: value.toExponential(2).replace("e", "\\times{10^{").replace("+", "") + "}}",
        operation: Operation.SCIENTIFIC
    };
}

function generateAdditionExpression(a, b) {
    return {
        value: a.value + b.value,
        latex: `{${a.latex}+${b.latex}}`,
        operation: Operation.ADDITION
    };
}

function generateSubtractionExpression(a, b) {
    let bLatex = b.latex;
    if (isInRange(b.operation, Operation.ADDITION, Operation.SUBTRACTION))
        bLatex = `\\left(${bLatex}\\right)`;
    return {
        value: a.value - b.value,
        latex: `{${a.latex}-${bLatex}}`,
        operation: Operation.SUBTRACTION,
    };
}

function generateMultiplicationExpression(a, b) {
    let aLatex = a.latex;
    let bLatex = b.latex;
    if (isInRange(a.operation, Operation.ADDITION, Operation.SUBTRACTION))
        aLatex = `\\left(${aLatex}\\right)`;
    if (isInRange(b.operation, Operation.ADDITION, Operation.SUBTRACTION))
        bLatex = `\\left(${bLatex}\\right)`;
    return {
        value: a.value * b.value,
        latex: `{${aLatex} \\times ${bLatex}}`,
        operation: Operation.MULTIPLICATION,
    };
}

function generateDivisionExpression(a, b) {
    let aLatex = a.latex;
    let bLatex = b.latex;
    if (isInRange(a.operation, Operation.ADDITION, Operation.SUBTRACTION))
        aLatex = `\\left(${aLatex}\\right)`;
    if (isInRange(b.operation, Operation.SCIENTIFIC, Operation.DIVISION))
        bLatex = `\\left(${bLatex}\\right)`;
    return {
        value: a.value / b.value,
        latex: `{${aLatex}/${bLatex}}`,
        operation: Operation.DIVISION,
    };
}

function generateExponentiationExpression(a, b) {
    let aLatex = a.latex;
    if (isInRange(a.operation, Operation.SCIENTIFIC, Operation.SQUARE_ROOT)) {
        aLatex = `\\left(${aLatex}\\right)`;
    }
    if (a.value < 0 && a.operation > Operation.PI)
        return {value: NaN, latex: "", operation: Operation.EXPONENTIATION};
    return {
        value: (a.value < 0 ? -1 : 1) * Math.pow(Math.abs(a.value), b.value),
        latex: `{{${aLatex}}^{${b.latex}}}`,
        operation: Operation.EXPONENTIATION,
    };
}

function generateFractionExpression(a, b) {
    return {
        value: a.value / b.value,
        latex: `{\\frac{${a.latex}}{${b.latex}}}`,
        operation: Operation.FRACTION,
    };
}

function generateSquareExpression(base) {
    let latex;
    if (isInRange(base.operation, Operation.ADDITION, Operation.FRACTION) || isInRange(base.operation, Operation.NUMBER, Operation.SCIENTIFIC) && base.value < 0) {
        latex = `{{\\left(${base.latex}\\right)}^{2}}`;
    } else {
        latex = `{{${base.latex}}^{2}}`
    }
    return {
        value: base.value * base.value,
        latex: latex,
        operation: Operation.SQUARE,
    };
}

function generateSquareRootExpression(radicand) {
    return {
        value: Math.sqrt(radicand.value),
        latex: `\\sqrt{${radicand.latex}}`,
        operation: Operation.SQUARE_ROOT,
    };
}

function generateNaturalLogExpression(argument) {
    return {
        value: Math.log(argument.value),
        latex: `\\ln\\left(${argument.latex}\\right)`,
        operation: Operation.NATURAL_LOG,
    };
}

function generateLog10Expression(argument) {
    return {
        value: Math.log(argument.value) / Math.log(10),
        latex: `\\log\\left(${argument.latex}\\right)`,
        operation: Operation.LOG_10,
    };
}

function generateSinExpression(angle) {
    return {
        value: Math.sin(angle.value),
        latex: `\\sin\\left(${angle.latex}\\right)`,
        operation: Operation.SIN,
    };
}

function generateCosExpression(angle) {
    return {
        value: Math.cos(angle.value),
        latex: `\\cos\\left(${angle.latex}\\right)`,
        operation: Operation.COS,
    };
}

function generateTanExpression(angle) {
    return {
        value: Math.tan(angle.value),
        latex: `\\tan\\left(${angle.latex}\\right)`,
        operation: Operation.TAN,
    };
}

function generateArcSinExpression(angle) {
    return {
        value: Math.asin(angle.value),
        latex: `\\arcsin\\left(${angle.latex}\\right)`,
        operation: Operation.ARCSIN,
    };
}

function generateArcCosExpression(angle) {
    return {
        value: Math.acos(angle.value),
        latex: `\\arccos\\left(${angle.latex}\\right)`,
        operation: Operation.ARCCOS,
    };
}

function generateArcTanExpression(angle) {
    return {
        value: Math.atan(angle.value),
        latex: `\\arctan\\left(${angle.latex}\\right)`,
        operation: Operation.ARCTAN,
    };
}

function isInRange(operation, minimum, maximum) {
    return operation >= minimum && operation <= maximum;
}

function generateExpressionTree(level, max, exponentialUsed = false, fractionsUsed = false) {
    if (level === max || Math.random() < 1 / 3) {
        if (Math.random() < 4 / 5) return generateNumberExpression();
        else if (Math.random() < 1 / 2 && !exponentialUsed)
            return generateScientificExpression();
        else if (Math.random() < 1 / 2) return generateEulerExpression();
        else return generatePiExpression();
    } else if (Math.random() < 9 / 10) {
        let randomOperation =
            Math.floor(
                Math.random() * (Operation.EXPONENTIATION - Operation.ADDITION + 1)
            ) + Operation.ADDITION;
        if (exponentialUsed && randomOperation === Operation.EXPONENTIATION)
            randomOperation =
                Math.floor(
                    Math.random() * (Operation.DIVISION - Operation.ADDITION + 1)
                ) + Operation.ADDITION;
        while (true) {
            let a = generateExpressionTree(
                level + 1,
                max,
                randomOperation === Operation.EXPONENTIATION || exponentialUsed,
                randomOperation === Operation.DIVISION || fractionsUsed
            );
            let b = generateExpressionTree(
                level + 1,
                max,
                randomOperation === Operation.EXPONENTIATION || exponentialUsed,
                randomOperation === Operation.DIVISION || fractionsUsed
            );
            if (a.value === b.value) continue;
            let expression;
            if (randomOperation === Operation.ADDITION)
                expression = generateAdditionExpression(a, b);
            else if (randomOperation === Operation.SUBTRACTION)
                expression = generateSubtractionExpression(a, b);
            else if (randomOperation === Operation.MULTIPLICATION)
                expression = generateMultiplicationExpression(a, b);
            else if (randomOperation === Operation.DIVISION) {
                if (fractionsUsed) expression = generateDivisionExpression(a, b);
                else expression = generateFractionExpression(a, b);
            } else expression = generateExponentiationExpression(a, b);
            if (isValidExpression(expression) && deltaCheck(expression, [a, b]))
                return expression;
        }
    } else {
        let randomOperation =
            Math.floor(Math.random() * (Operation.ARCTAN - Operation.SQUARE + 1)) +
            Operation.SQUARE;
        if (randomOperation === Operation.SQUARE && exponentialUsed)
            randomOperation =
                Math.floor(
                    Math.random() * (Operation.ARCTAN - Operation.SQUARE_ROOT + 1)
                ) + Operation.SQUARE_ROOT;
        while (true) {
            let operand = generateExpressionTree(
                level + 1,
                max,
                randomOperation === Operation.SQUARE || exponentialUsed,
                fractionsUsed
            );
            let expression;
            if (randomOperation === Operation.SQUARE)
                expression = generateSquareExpression(operand);
            else if (randomOperation === Operation.SQUARE_ROOT)
                expression = generateSquareRootExpression(operand);
            else if (randomOperation === Operation.NATURAL_LOG)
                expression = generateNaturalLogExpression(operand);
            else if (randomOperation === Operation.LOG_10)
                expression = generateLog10Expression(operand);
            else if (randomOperation === Operation.SIN)
                expression = generateSinExpression(operand);
            else if (randomOperation === Operation.COS)
                expression = generateCosExpression(operand);
            else if (randomOperation === Operation.TAN)
                expression = generateTanExpression(operand);
            else if (randomOperation === Operation.ARCSIN)
                expression = generateArcSinExpression(operand);
            else if (randomOperation === Operation.ARCCOS)
                expression = generateArcCosExpression(operand);
            else expression = generateArcTanExpression(operand);
            if (isValidExpression(expression) && deltaCheck(expression, [operand]))
                return expression;
        }
    }
}

function isValidExpression(expression) {
    return (
        expression.latex.length <= 125 &&
        !isNaN(expression.value) &&
        isFinite(expression.value) &&
        Math.abs(expression.value) < Math.pow(10, 10) &&
        Math.abs(expression.value) > Math.pow(10, -10) &&
        expression.value !== undefined
    );
}

function deltaCheck(result, operands) {
    let formattedResult = result.value.toExponential(2);
    for (let i = 0; i < operands.length; i++) {
        if (formattedResult === operands[i].value.toExponential(2)) return false;
    }
    return true;
}

function generateExpression() {
    let expression;
    while (true) {
        expression = generateExpressionTree(0, 5);
        if (expression.latex.length >= 75) break;
    }
    expression.latex = expression.latex
        .replace(/\+\s*-/g, "-")
        .replace(/-\s*-/g, "+")
        .replace(/\+\s*{\s*-/g, "-{")
        .replace(/-\s*{\s*-/g, "+{")
        .replace(/\+\s*{{\s*-/g, "-{{")
        .replace(/-\s*{{\s*-/g, "+{{")
        .replace(/\+\s*{{{\s*-/g, "-{{{")
        .replace(/-\s*{{{\s*-/g, "+{{{")
        .replace(/\+\s*{{{{\s*-/g, "-{{{{")
        .replace(/-\s*{{{{\s*-/g, "+{{{{");
    return expression;
}