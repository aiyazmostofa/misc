#include <algorithm>
#include <limits>
#include <stack>

constexpr int ROWS = 6;
constexpr int COLUMNS = 7;
constexpr int DEPTH = 7;
constexpr double POSITIVE_INFINITY = std::numeric_limits<double>::infinity();
constexpr double NEGATIVE_INFINITY = -std::numeric_limits<double>::infinity();

class Engine {
  private:
    struct Result {
        double score;
        int depth;
    };

    int blankSpots[COLUMNS];
    std::stack<int> indicesPlayed;
    int moves;

    Result minimax(char matrix[ROWS][COLUMNS], int limit, double alpha,
                   double beta, bool maximizing) {
        if (hasWinner(matrix)) {
            double score = maximizing ? NEGATIVE_INFINITY : POSITIVE_INFINITY;
            return Result{score, DEPTH - limit};
        }
        if (moves == COLUMNS * ROWS)
            return Result{0.0, DEPTH - limit};
        if (limit == 0) {
            return Result{evaluate(matrix), DEPTH};
        }

        double result;
        int maxMinDepth = maximizing ? DEPTH : 0;
        if (maximizing) {
            result = NEGATIVE_INFINITY;
            for (int i = 0; i < COLUMNS; i++) {
                if (blankSpots[i] == -1)
                    continue;
                add(matrix, i, true);
                auto temp = minimax(matrix, limit - 1, alpha, beta, false);
                remove(matrix, i);
                if (temp.score > result ||
                    temp.score == result && temp.depth < maxMinDepth) {
                    result = temp.score;
                    maxMinDepth = temp.depth;
                }

                alpha = std::max(alpha, temp.score);
                if (beta <= alpha)
                    break;
            }
        } else {
            result = POSITIVE_INFINITY;
            for (int i = 0; i < COLUMNS; i++) {
                if (blankSpots[i] == -1)
                    continue;
                add(matrix, i, false);
                auto temp = minimax(matrix, limit - 1, alpha, beta, true);
                remove(matrix, i);
                if (temp.score < result ||
                    temp.score == result && temp.depth > maxMinDepth) {
                    result = temp.score;
                    maxMinDepth = temp.depth;
                }

                beta = std::min(beta, temp.score);
                if (beta <= alpha)
                    break;
            }
        }
        return Result{result, maxMinDepth};
    }

    void add(char matrix[ROWS][COLUMNS], int column, bool self) {
        while (blankSpots[column] == -1)
            column++;
        matrix[blankSpots[column]][column] = self ? 'X' : 'O';
        moves++;
        blankSpots[column]--;
        indicesPlayed.push(column);
    }

    void remove(char matrix[ROWS][COLUMNS], int column) {
        blankSpots[column]++;
        moves--;
        matrix[blankSpots[column]][column] = '-';
        indicesPlayed.pop();
    }

    bool hasWinner(char matrix[ROWS][COLUMNS]) {
        int c = indicesPlayed.top();
        int r = blankSpots[c] + 1;

        int x;
        int o;

        x = o = 0;
        for (int i = -3; i <= 3; i++) {
            if (c + i < 0 || c + i >= COLUMNS)
                continue;
            if (matrix[r][c + i] == 'X') {
                x++;
                o = 0;
            }
            if (matrix[r][c + i] == 'O') {
                x = 0;
                o++;
            }
            if (matrix[r][c + i] == '-') {
                x = o = 0;
            }
            if (x == 4 || o == 4)
                return true;
        }

        x = o = 0;
        for (int i = -3; i <= 3; i++) {
            if (r + i < 0 || r + i >= ROWS)
                continue;
            if (matrix[r + i][c] == 'X') {
                x++;
                o = 0;
            }
            if (matrix[r + i][c] == 'O') {
                x = 0;
                o++;
            }
            if (matrix[r + i][c] == '-') {
                x = o = 0;
            }
            if (x == 4 || o == 4)
                return true;
        }

        x = o = 0;
        for (int i = -3; i <= 3; i++) {
            if (r + i < 0 || r + i >= ROWS || c + i < 0 || c + i >= COLUMNS)
                continue;
            if (matrix[r + i][c + i] == 'X') {
                x++;
                o = 0;
            }
            if (matrix[r + i][c + i] == 'O') {
                x = 0;
                o++;
            }
            if (matrix[r + i][c + i] == '-') {
                x = o = 0;
            }
            if (x == 4 || o == 4)
                return true;
        }

        x = o = 0;
        for (int i = -3; i <= 3; i++) {
            if (r + i < 0 || r + i >= ROWS || c - i < 0 || c - i >= COLUMNS)
                continue;
            if (matrix[r + i][c - i] == 'X') {
                x++;
                o = 0;
            }
            if (matrix[r + i][c - i] == 'O') {
                x = 0;
                o++;
            }
            if (matrix[r + i][c - i] == '-') {
                x = o = 0;
            }
            if (x == 4 || o == 4)
                return true;
        }
        return false;
    }

    double evaluate(char matrix[ROWS][COLUMNS]) {
        int oneAwayForSelf = 0;
        int twoAwayForSelf = 0;
        int oneAwayForOther = 0;

        for (int r = 0; r < ROWS; r++) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int c = 0; c < COLUMNS; c++) {
                if (matrix[r][c] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[r][c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[r][c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[r][c] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled >= 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[r][c - (blanks + filled - 1)] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[r][c];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        for (int c = 0; c < COLUMNS; c++) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int r = 0; r < ROWS; r++) {
                if (matrix[r][c] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[r - (blanks + filled - 1)][c] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[r - (blanks + filled - 1)][c] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[r][c] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled == 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[r - (blanks + filled - 1)][c] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[r][c];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        for (int r = 0; r < ROWS - 1; r++) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int c = 0; c < COLUMNS && r + c < ROWS; c++) {
                int rc = r + c;
                if (matrix[rc][c] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[rc - (blanks + filled - 1)]
                                  [c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[rc - (blanks + filled - 1)]
                                  [c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[rc][c] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled == 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[rc - (blanks + filled - 1)]
                                      [c - (blanks + filled - 1)] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[rc][c];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        for (int c = 1; c < COLUMNS; c++) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int r = 0; r < ROWS && r + c < COLUMNS; r++) {
                int rc = r + c;
                if (matrix[r][rc] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[r - (blanks + filled - 1)]
                                  [rc - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[r - (blanks + filled - 1)]
                                  [rc - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[r][rc] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled == 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[r - (blanks + filled - 1)]
                                      [rc - (blanks + filled - 1)] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[r][rc];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        for (int r = ROWS - 1; r >= 0; r--) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int c = 0; c < COLUMNS && r - c >= 0; c++) {
                int rc = r - c;
                if (matrix[rc][c] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[rc + (blanks + filled - 1)]
                                  [c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[rc + (blanks + filled - 1)]
                                  [c - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[rc][c] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled == 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[rc + (blanks + filled - 1)]
                                      [c - (blanks + filled - 1)] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[rc][c];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        for (int c = 1; c < COLUMNS; c++) {
            int blanks = 0;
            int tailBlanks = 0;
            int filled = 0;
            char current = '\u0000';
            for (int r = ROWS - 1; r >= 0 && c + (ROWS - 1 - r) < COLUMNS;
                 r--) {
                int rc = (ROWS - 1 - r) + c;
                if (matrix[r][rc] == '-') {
                    blanks++;
                    tailBlanks++;
                    if (blanks >= 3) {
                        if (matrix[r + (blanks + filled - 1)]
                                  [rc - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                    if (blanks + filled == 4) {
                        if (current == 'X') {
                            if (blanks == 1) {
                                oneAwayForSelf++;
                            } else
                                twoAwayForSelf++;
                        } else {
                            if (blanks == 1)
                                oneAwayForOther++;
                        }
                        if (matrix[r + (blanks + filled - 1)]
                                  [rc - (blanks + filled - 1)] == '-')
                            blanks--;
                        else
                            filled--;
                        if (tailBlanks > blanks)
                            tailBlanks = blanks;
                    }
                } else {
                    if (matrix[r][rc] == current) {
                        filled++;
                        tailBlanks = 0;
                        if (filled == 4)
                            return current == 'X' ? POSITIVE_INFINITY
                                                  : NEGATIVE_INFINITY;
                        if (blanks + filled == 4) {
                            if (current == 'X') {
                                if (blanks == 1) {
                                    oneAwayForSelf++;
                                } else
                                    twoAwayForSelf++;
                            } else {
                                if (blanks == 1)
                                    oneAwayForOther++;
                            }
                            if (matrix[r + (blanks + filled - 1)]
                                      [rc - (blanks + filled - 1)] == '-')
                                blanks--;
                            else
                                filled--;
                            if (tailBlanks > blanks)
                                tailBlanks = blanks;
                        }
                    } else {
                        current = matrix[r][rc];
                        blanks = tailBlanks;
                        tailBlanks = 0;
                        filled = 1;
                    }
                }
            }
        }

        return 125.0 * oneAwayForSelf + 64.0 * twoAwayForSelf -
               64.0 * oneAwayForOther;
    }

  public:
    int compute(char matrix[ROWS][COLUMNS]) {
        for (int i = 0; i < COLUMNS; i++) {
            blankSpots[i] = 0;
        }
        indicesPlayed = std::stack<int>();
        moves = 0;
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = ROWS - 1; j >= -1; j--) {
                if (j != -1 && matrix[j][i] != '-') {
                    moves++;
                    continue;
                }
                blankSpots[i] = j;
                break;
            }
        }

        if (blankSpots[COLUMNS / 2] >= ROWS - 2) {
            return COLUMNS / 2;
        }

        int index = 0;
        double max = NEGATIVE_INFINITY;
        int minDepth = DEPTH;
        for (int i = 0; i < COLUMNS; i++) {
            if (blankSpots[i] == -1) {
                if (index == i)
                    index++;
                continue;
            }
            add(matrix, i, true);
            auto temp = minimax(matrix, DEPTH, NEGATIVE_INFINITY,
                                POSITIVE_INFINITY, false);
            remove(matrix, i);
            if (temp.score > max ||
                temp.score == max && temp.depth < minDepth) {
                index = i;
                max = temp.score;
                minDepth = temp.depth;
            }
        }

        return index;
    }
};

// https://developer.mozilla.org/en-US/docs/WebAssembly/Guides/C_to_Wasm#calling_a_custom_function_defined_in_c
#include <emscripten/emscripten.h>
extern "C" EMSCRIPTEN_KEEPALIVE int compute(const char *board) {
    char matrix[ROWS][COLUMNS];
    for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 7; j++) {
            matrix[i][j] = board[i * 7 + j];
        }
    }
    Engine e;
    return e.compute(matrix);
}
