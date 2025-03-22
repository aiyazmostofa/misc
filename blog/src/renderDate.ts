export default function (date: Date): string {
    const nthNumber = (n: number) => {
        if (n > 3 && n < 21) return "th";
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    };
    const month = date.toLocaleString("default", {
        month: "long",
        timeZone: 'UTC'
    });
    const day = date.getUTCDate();
    const suffix = nthNumber(day);
    const year = date.getUTCFullYear();

    return `${month} ${day}${suffix}, ${year}`;
}