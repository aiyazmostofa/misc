// The old 'index.js' was an axios web server.
// This is no longer needed.
// However, we keep it because it minimizes change.

import createModule from "./bin/engine.mjs";

function verify(matrix) {
    for (let i = 0; i < 7; i++) {
        let reached = false;
        for (let j = 0; j < 6; j++) {
            if (matrix[j][i] !== "-") {
                reached = true;
            }
            if (matrix[j][i] === "-" && reached) {
                return false;
            }
        }
    }
    return true;
}

export default async function call(board, player) {
    if ((player !== "R" && player !== "Y") || board.length !== 42 || !board.includes("-")) {
        return "Send a valid board.";
    } else {
        let matrix = new Array(6);
        for (let i = 0; i < 6; i++) {
            matrix[i] = new Array(7);
            for (let j = 0; j < 7; j++) {
                let value = board.charAt(i * 7 + j);
                if (value !== "-" && value !== "R" && value !== "Y") {
                    return "Send a valid board.";
                }
                matrix[i][j] = value;
            }
        }
        let result = verify(matrix);
        if (!result) {
            return "Send a valid board.";
        }
        if (player === "R") {
            board = board.replace(/R/g, "X");
            board = board.replace(/Y/g, "O");
        } else {
            board = board.replace(/Y/g, "X");
            board = board.replace(/R/g, "O");
        }

        const Module = await createModule();
        const move = Module.ccall("compute", null, ["string"], [board]);
        return { move: move };
    }
}
