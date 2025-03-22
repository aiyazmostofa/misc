import { Card, Placement, NewCard, SuitData, Value, ValueData } from "./typedef";
import './style.css'

let currentPlayer = 0 // Current player, mapped to playerOrdering constant
let winner = "" // Winner status, if > 0, then there is a winner
let winnerDialogOpen = false // Whether the winner dialog is open
let cards: Card[] = []; // All of the cards
let cardElements: HTMLElement[] = [] // All of the elements for each cards
let selectedCard: Card | undefined = undefined // The selected card that will be moved
let availablePiles: Placement[] = [] // Piles that the selected card can be moved to

const playerOrder = [Placement.SOUTH_PLAYER, Placement.WEST_PLAYER, Placement.NORTH_PLAYER, Placement.EAST_PLAYER] // The order the players will move
const htmlSelectorIDs = ['e', 'ne', 'n', 'nw', 'w', 'sw', 's', 'se'] // DOM ID's for pile selectors
const playPiles = [
    Placement.EAST_PILE,
    Placement.NORTHEAST_PILE,
    Placement.NORTH_PILE,
    Placement.NORTHWEST_PILE,
    Placement.WEST_PILE,
    Placement.SOUTHWEST_PILE,
    Placement.SOUTH_PILE,
    Placement.SOUTHEAST_PILE
] // All piles that aren't the center pile or any of the player's piles

// See if the player clicked a card that they can select.
function validSelection(card: Card): boolean {
    return playerOrder[currentPlayer] == card.placement || playPiles.indexOf(card.placement) >= 0
}

// Render the cards in the center.
function init() {
    for (let i = 0; i < 13; i++)
        for (let j = 0; j < 4; j++)
            cards.push(NewCard(i, j, Placement.CENTER_PILE, 0))

    for (let i = 0; i < 52; i++) {
        render(cards[i], -1)
    }
}

// Set selected card and do associated renders
function setSelectedCard(_first: Card | undefined) {
    selectedCard = _first
    if (selectedCard == undefined)
        availablePiles = [];
    (<HTMLInputElement>document.getElementById("escape"))!.disabled = selectedCard == undefined
}

// Generate all possible selections after a selected card has been chosen.
function generateSelections() {
    if (selectedCard == undefined) return
    // If we selected a pile.
    if (playPiles.indexOf(selectedCard.placement) >= 0) {
        // All cards in our selected pile.
        let ourPile = cards.filter((card) => {
            return selectedCard?.placement == card.placement
        }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })

        for (let i = 0; i < 8; i++) {
            if (playPiles[i] == selectedCard.placement) continue
            // All cards in potential pile.
            let nextPile = cards.filter((card) => {
                return playPiles[i] == card.placement
            }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })
            if (nextPile.length == 0)
                continue;
            else {
                if (ValueData[nextPile[nextPile.length - 1].value].rank == (ValueData[ourPile[0].value].rank + 1) % 13 && SuitData[nextPile[nextPile.length - 1].suit].color != SuitData[ourPile[0].suit].color) {
                    availablePiles.push(i)
                }
            }
        }
    } else { // If we selected a card.
        for (let i = 0; i < 8; i++) {
            let nextPile = cards.filter((card) => {
                return playPiles[i] == card.placement
            }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })
            if (nextPile.length == 0) {
                // Odd vertices are corner piles, so they need an extra check.
                if (selectedCard.value == Value.KING && i % 2 == 1 || i % 2 == 0) {
                    availablePiles.push(i)
                }
                continue;
            }
            else {
                if (ValueData[nextPile[nextPile.length - 1].value].rank == (ValueData[selectedCard.value].rank + 1) % 13 && SuitData[nextPile[nextPile.length - 1].suit].color != SuitData[selectedCard.suit].color) {
                    availablePiles.push(i)
                }
            }
        }
    }

    update()
}

// Insert card (or modify) Card div element in the DOM.
function render(card: Card, index: number) {
    var element: HTMLElement; // Create element
    // If it's a first time render
    if (index == -1) {
        element = document.createElement("div")
        element.onclick = () => {
            if (currentPlayer != 0 || winner.length > 0) return
            if (selectedCard != undefined) return
            if (validSelection(card)) {
                setSelectedCard(card)
                generateSelections()
            }
        } // Add click listener to the card
        element.classList.add("card")
        element.classList.add(SuitData[card.suit].color)

        // Renders the top left legend
        let inner = `<div class="legend">
        <span class="text">${ValueData[card.value].unicode}</span>
        <span>${SuitData[card.suit].unicode}</span>
        </div>\n<div>`

        // Renders the design
        for (let i = 0; i < ValueData[card.value].design.length; i++) {
            let innerInner = `<div class="card-row">\n`
            for (let j = 0; j < ValueData[card.value].design[i].length; j++) {
                if (ValueData[card.value].design[i][j] == '*')
                    innerInner += `<span>${SuitData[card.suit].unicode}</span>\n`
                else
                    innerInner += `<span>&nbsp</span>\n`
            }
            innerInner += `</div>\n`
            inner += innerInner
        }

        // Renders the bottom right legend
        inner += `</div><div class="upside-down legend">
        <span class="text">${ValueData[card.value].unicode}</span>
        <span>${SuitData[card.suit].unicode}</span>
        </div>`

        // Inject into DOM"src", 
        element.innerHTML = inner
        cardElements.push(element)
        document.getElementById("game")!.appendChild(element)
    } else
        element = cardElements[index]

    element.style.zIndex = `${card.index}` // Update zIndex

    // Conceal/reveal depending on game state
    if ((playPiles.indexOf(card.placement) >= 0 || card.placement == Placement.SOUTH_PLAYER || winner.length > 0) && card.placement != Placement.CENTER_PILE) element.classList.remove("concealed")
    else element.classList.add("concealed")

    // Update position based on game state
    if ((playPiles.indexOf(card.placement) >= 0 || card.placement == Placement.SOUTH_PLAYER || winner.length > 0) && card.placement != Placement.CENTER_PILE) element.classList.remove("concealed")
    let position = calculatePosition(card)
    element.style.top = `${position[1]}px`
    element.style.left = `${position[0]}px`
    element.style.rotate = `${position[2]}deg`
}

// Calculate position based on game state.
function calculatePosition(card: Card): Number[] {
    switch (card.placement) {
        case Placement.CENTER_PILE:
            return [640, 360, 0]
        case Placement.NORTH_PILE:
            // There are 2 possible indices, so choose one or the other.
            return card.index == 0 ? [640, 260, 180] : [640, 230, 180]
        case Placement.SOUTH_PILE:
            return card.index == 0 ? [640, 460, 0] : [640, 490, 0]
        case Placement.EAST_PILE:
            return card.index == 0 ? [740, 360, 270] : [770, 360, 270]
        case Placement.WEST_PILE:
            return card.index == 0 ? [540, 360, 90] : [510, 360, 90]
        case Placement.NORTHWEST_PILE:
            return card.index == 0 ? [530, 250, 135] : [510, 230, 135]
        case Placement.SOUTHWEST_PILE:
            return card.index == 0 ? [530, 470, 45] : [510, 490, 45]
        case Placement.NORTHEAST_PILE:
            return card.index == 0 ? [750, 250, 225] : [770, 230, 225]
        case Placement.SOUTHEAST_PILE:
            return card.index == 0 ? [750, 470, 315] : [770, 490, 315]
        case Placement.NORTH_PLAYER:
            // Find all the other cards in the same pile, and calculate the relative position
            let northPlayer = cards.filter((card) => card.placement == Placement.NORTH_PLAYER).sort((a, b) => a.index.valueOf() - b.index.valueOf())
            if (northPlayer.length % 2 == 0) {
                if (card.index.valueOf() < northPlayer.length / 2) {
                    return [620 + (card.index.valueOf() - northPlayer.length / 2 + 1) * 40, 100, 180]
                } else {
                    return [660 + (card.index.valueOf() - northPlayer.length / 2) * 40, 100, 180]
                }
            } else {
                return [640 + (card.index.valueOf() - Math.floor(northPlayer.length / 2)) * 40, 100, 180]
            }
        case Placement.SOUTH_PLAYER:
            let southPlayer = cards.filter((card) => card.placement == Placement.SOUTH_PLAYER).sort((a, b) => a.index.valueOf() - b.index.valueOf())
            if (southPlayer.length % 2 == 0) {
                if (card.index.valueOf() < southPlayer.length / 2) {
                    return [600 + (card.index.valueOf() - southPlayer.length / 2 + 1) * 80, 620, 0]
                } else {
                    return [680 + (card.index.valueOf() - southPlayer.length / 2) * 80, 620, 0]
                }
            } else {
                return [640 + (card.index.valueOf() - Math.floor(southPlayer.length / 2)) * 80, 620, 0]
            }
        case Placement.EAST_PLAYER:
            let eastPlayer = cards.filter((card) => card.placement == Placement.EAST_PLAYER).sort((a, b) => a.index.valueOf() - b.index.valueOf())
            if (eastPlayer.length % 2 == 0) {
                if (card.index.valueOf() < eastPlayer.length / 2) {
                    return [1180, 340 + (card.index.valueOf() - eastPlayer.length / 2 + 1) * 40, 90]
                } else {
                    return [1180, 380 + (card.index.valueOf() - eastPlayer.length / 2) * 40, 90]
                }
            } else {
                return [1180, 360 + (card.index.valueOf() - Math.floor(eastPlayer.length / 2)) * 40, 90]
            }

        case Placement.WEST_PLAYER:
            let westPlayer = cards.filter((card) => card.placement == Placement.WEST_PLAYER).sort((a, b) => a.index.valueOf() - b.index.valueOf())
            if (westPlayer.length % 2 == 0) {
                if (card.index.valueOf() < westPlayer.length / 2) {
                    return [100, 340 + (card.index.valueOf() - westPlayer.length / 2 + 1) * 40, 90]
                } else {
                    return [100, 380 + (card.index.valueOf() - westPlayer.length / 2) * 40, 90]
                }
            } else {
                return [100, 360 + (card.index.valueOf() - Math.floor(westPlayer.length / 2)) * 40, 90]
            }
        default:
            return [0, 0, 0]
    }
}

// Update DOM according to game state
function update() {
    for (let i = 0; i < 52; i++) {
        render(cards[i], i)
    }

    // All selection IDs that aren't eventually in visited need to be hidden.
    let visited: Set<Number> = new Set()
    for (let i = 0; i < availablePiles.length; i++) {
        const ele = document.getElementById(htmlSelectorIDs[availablePiles[i]])
        visited.add(availablePiles[i])
        ele!.style.zIndex = `100`
    }

    for (let i = 0; i < 8; i++) {
        if (visited.has(i)) continue;
        const ele = document.getElementById(htmlSelectorIDs[i])
        ele!.style.zIndex = `-100`
    }

    document.getElementById("winner-card")!.style.zIndex = winnerDialogOpen ? `100` : `-100`;
    document.getElementById("winner-card-text")!.innerText = winner;
}

// Play card/pile
// A card that is in a pile will be considered as the entire pile
function playMove(fromcard: Card, placementID: number) {
    const placement = playPiles[placementID]
    if (playPiles.indexOf(fromcard.placement) >= 0) {
        let fromPile = cards.filter((card) => {
            return card.placement == fromcard.placement
        }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })

        let toPile = cards.filter((card) => {
            return card.placement == placement
        }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })

        if (fromPile.length > 1) {
            fromPile[0].placement = Placement.CENTER_PILE
            fromPile[0].index = 0
            fromPile[1].placement = placement
            fromPile[1].index = 1
        } else {
            fromPile[0].placement = placement
            fromPile[0].index = 1
        }

        if (toPile.length > 1) {
            toPile[1].placement = Placement.CENTER_PILE
            toPile[1].index = 0
        }
    } else {
        let fromPlayer = cards.filter((card) => card.placement == fromcard.placement).sort((a, b) => a.index.valueOf() - b.index.valueOf())
        let toPile = cards.filter((card) => card.placement == placement).sort((a, b) => a.index.valueOf() - b.index.valueOf())

        // Update the player's deck so the indices are consecutive, since it's used in rendering
        for (let i = 0; i < fromPlayer.length; i++)
            if (fromPlayer[i].index > fromcard.index) fromPlayer[i].index = (Number)(fromPlayer[i].index.valueOf() - 1);
        fromcard.placement = placement
        fromcard.index = toPile.length
        if (toPile.length > 1) {
            toPile[1].placement = Placement.CENTER_PILE
            toPile[1].index = 0
        }

        if (fromPlayer.length == 1) {
            setWinner(currentPlayer)
        }
    }
    setSelectedCard(undefined)
    update()
}

// Distribute cards
function distribute() {
    // All actions happen 100ms from each other, so timeout is needed
    for (let i = 0; i < 7; i++) {
        for (let j = 0; j < 4; j++) {
            let index = i * 4 + j
            setTimeout(() => {
                let card = randomPileCard()
                card!.index = i
                card!.placement = playerOrder[j]
                update()
            }, 100 * index)
        }
    }

    setTimeout(() => {
        randomPileCard()!.placement = Placement.NORTH_PILE
        update()
    }, 100 * 28)

    setTimeout(() => {
        randomPileCard()!.placement = Placement.SOUTH_PILE
        update()
    }, 100 * 29)

    setTimeout(() => {
        randomPileCard()!.placement = Placement.WEST_PILE
        update()
    }, 100 * 30)

    setTimeout(() => {
        randomPileCard()!.placement = Placement.EAST_PILE
        update()
    }, 100 * 31)

    setTimeout(() => {
        let card = randomPileCard()!
        card.placement = Placement.SOUTH_PLAYER
        card.index = 7
        update()
    }, 100 * 32)
}

// Choose a random card from the pile.
function randomPileCard(): Card | undefined {
    if (cards.filter((card) => card.placement == Placement.CENTER_PILE).length == 0) return undefined
    let index = Math.floor(Math.random() * 52)
    while (cards[index].placement != Placement.CENTER_PILE) index = Math.floor(Math.random() * 52)
    return cards[index]
}

// Update the game logic to make x a winner
function setWinner(x: number) {
    winner = `Player ${x + 1} is the winner!`
    document.getElementById("turn-indicator")!.remove()
    winnerDialogOpen = true;
    (<HTMLInputElement>document.getElementById("next"))!.disabled = true;
    (<HTMLInputElement>document.getElementById("escape"))!.disabled = true
}

// Distribute a single card to the current player.
function giveCard() {
    if (winner.length > 0) return
    let newCards = cards.filter((card) => {
        return card.placement == playerOrder[currentPlayer]
    })

    let newCard = randomPileCard()
    if (newCard == undefined) return
    newCard.index = newCards.length
    newCard.placement = playerOrder[currentPlayer]
    update()
}

// Mirror of generateSelections but for the AI.
// Up to 10 times, play the first possible move seen.
// See comments for generateSelections for explanation
function generateAIMove() {
    for (let k = 0; k < 10; k++) {
        if (winner.length > 0) return
        let playedCard = true;
        let playerCards = cards.filter((card) => {
            return card.placement == playerOrder[currentPlayer]
        });
        for (let j = 0; j < playerCards.length && playedCard; j++) {
            for (let i = 0; i < 8 && playedCard; i++) {
                let nextPile = cards.filter((card) => {
                    return playPiles[i] == card.placement
                }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })
                if (nextPile.length == 0) {
                    if (playerCards[j].value == Value.KING && i % 2 == 1 || i % 2 == 0) {
                        playMove(playerCards[j], i)
                        playedCard = false
                    }
                }
                else {
                    if (ValueData[nextPile[nextPile.length - 1].value].rank - 1 == ValueData[playerCards[j].value].rank && SuitData[nextPile[nextPile.length - 1].suit].color != SuitData[playerCards[j].suit].color) {
                        playMove(playerCards[j], i)
                        playedCard = false
                    }
                }
            }

            for (let j = 0; j < 8 && playedCard; j++) {
                let ourPile = cards.filter((card) => {
                    return playPiles[j] == card.placement
                }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })
                if (ourPile.length == 0) continue;
                for (let i = 0; i < 8 && playedCard; i++) {
                    if (i == j) continue;
                    if (playPiles[i] == playPiles[j]) continue
                    let nextPile = cards.filter((card) => {
                        return playPiles[i] == card.placement
                    }).sort((a, b) => { return a.index.valueOf() - b.index.valueOf() })
                    if (nextPile.length == 0)
                        continue;
                    else {
                        if (ValueData[nextPile[nextPile.length - 1].value].rank - 1 == ValueData[ourPile[0].value].rank && SuitData[nextPile[nextPile.length - 1].suit].color != SuitData[ourPile[0].suit].color) {
                            playMove(ourPile[0], i)
                        }
                    }
                }
            }
        }
    }

}

/* 
    Starting point for code execution
*/

// Add click listeners to all pile selections
for (let i = 0; i < 8; i++) {
    const ele = document.getElementById(htmlSelectorIDs[i])
    ele!.onclick = () => {
        if (currentPlayer != 0 || winner.length > 0) return
        playMove(selectedCard!, i)
    }
}

// Add click listener to winner dialog close button
document.getElementById("winner-card-close")!.onclick = () => {
    winnerDialogOpen = false
    update()
}

// Add click listener to next button
document.getElementById("next")!.onclick = () => {
    setSelectedCard(undefined)
    /* Cycle:
        change status
        distribute card
        wait 1000ms
        play move
        wait 1000ms
        repeat to next player
    */

    currentPlayer = 1;
    (<HTMLInputElement>document.getElementById("next"))!.disabled = true
    document.getElementById("turn-indicator")!.innerText = `Player ${currentPlayer + 1}'s Turn`
    giveCard()
    setTimeout(() => {
        generateAIMove()
    }, 1000)

    setTimeout(() => {
        currentPlayer = 2
        document.getElementById("turn-indicator")!.innerText = `Player ${currentPlayer + 1}'s Turn`
        giveCard()
    }, 2000)

    setTimeout(() => {
        generateAIMove()
    }, 3000)

    setTimeout(() => {
        currentPlayer = 3
        document.getElementById("turn-indicator")!.innerText = `Player ${currentPlayer + 1}'s Turn`
        giveCard()
    }, 4000)

    setTimeout(() => {
        generateAIMove()
    }, 5000)

    setTimeout(() => {
        currentPlayer = 0
        document.getElementById("turn-indicator")!.innerText = `Player ${currentPlayer + 1}'s Turn`;
        (<HTMLInputElement>document.getElementById("next"))!.disabled = false
        giveCard()
    }, 6000)
}

// Add click listener to escape selection button
document.getElementById("escape")!.onclick = () => {
    setSelectedCard(undefined)
    update()
}

// Disable escape seleciton button by default
(<HTMLInputElement>document.getElementById("escape"))!.disabled = true

init()
distribute()
