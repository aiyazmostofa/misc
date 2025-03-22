// Suits
export enum Suit {
    CLUB,
    DIAMOND,
    HEART,
    SPADE
}

// For each suit, what are the associated traits?
// This will include unicode value for the suit, and the color.
export const SuitData = [
    {
        unicode: "♣",
        color: "black"
    },
    {
        unicode: "♦",
        color: "red"
    },
    {
        unicode: "♥",
        color: "red"
    },
    {
        unicode: "♠",
        color: "black"
    }
]

// Card values
export enum Value {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
}

// For each card value, what are the associated traits?
// This will include unicode value for the card, it's rank, and a matrix containing a design.
export const ValueData = [
    {
        unicode: "A",
        rank: 1,
        design: [
            ['*']
        ]
    },
    {
        unicode: "2",
        rank: 2,
        design: [
            ['*'],
            ['*']
        ]
    },
    {
        unicode: "3",
        rank: 3,
        design:
            [
                ['*'],
                ['*', '*'],
            ]
    },
    {
        unicode: "4",
        rank: 4,
        design: [
            ['*', '*'],
            ['*', '*']
        ],
    },
    {
        unicode: "5",
        rank: 5,
        design: [
            ['*'],
            ['*', '*', '*'],
            ['*']
        ]
    },
    {
        unicode: "6",
        rank: 6,
        design: [
            ['*', '*'],
            ['*', '*'],
            ['*', '*']
        ]
    },
    {
        unicode: "7",
        rank: 7,
        design: [
            ['*', '*'],
            ['*', '*', '*'],
            ['*', '*'],
        ],
    },
    {
        unicode: "8",
        rank: 8,
        design: [
            ['*', '*', '*'],
            ['*', ' ', '*'],
            ['*', '*', '*'],
        ],
    },
    {
        unicode: "9",
        rank: 9,
        design: [
            ['*', '*', '*'],
            ['*', '*', '*'],
            ['*', '*', '*'],
        ],
    },
    {
        unicode: "10",
        rank: 10,
        design: [
            ['*', '*', '*'],
            ['*', '*', '*', '*'],
            ['*', '*', '*'],
        ],
    },
    {
        unicode: "J",
        rank: 11,
        design: [
            ['*', '*', '*'],
            ['*', '*', '*', '*', '*'],
            ['*', '*', '*'],
        ],
    },
    {
        unicode: "Q",
        rank: 12,
        design: [
            ['*'],
            ['*', '*', '*'],
            ['*', '*', ' ', '*', '*'],
            ['*', '*', '*'],
            ['*'],
        ]
    },
    {
        unicode: "K",
        rank: 13,
        design: [
            ['*'],
            ['*', '*', '*'],
            ['*', '*', '*', '*', '*'],
            ['*', '*', '*'],
            ['*'],
        ],
    },
]

// Where can a card go?
// In these positions.
export enum Placement {
    CENTER_PILE,
    SOUTH_PLAYER,
    NORTH_PLAYER,
    EAST_PLAYER,
    WEST_PLAYER,
    NORTHWEST_PILE,
    SOUTHWEST_PILE,
    NORTHEAST_PILE,
    SOUTHEAST_PILE,
    NORTH_PILE,
    SOUTH_PILE,
    EAST_PILE,
    WEST_PILE,
}

// Self explanatory, except index.
// Index is where in the placement the card is present.
export interface Card {
    value: Value
    suit: Suit
    placement: Placement
    index: Number
}

// Type safe constructor
export function NewCard(value: Value, suit: Suit, placement: Placement, index: Number): Card {
    return { value, suit, placement, index }
}