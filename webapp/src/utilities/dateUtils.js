export const monthString = {
    1: 'January',
    2: 'February',
    3: 'March',
    4: 'April',
    5: 'May',
    6: 'June',
    7: 'July',
    8: 'August',
    9: 'September',
    10: 'October',
    11: 'November',
    12: 'December'
};

export const dayString = {
    0: 'Sunday',
    1: 'Monday',
    2: 'Tuesday',
    3: 'Wednesday',
    4: 'Thursday',
    5: 'Friday',
    6: 'Saturday'
};

export const dateFormatter = {
    hyphenatedYearMonthDay: function(date: Date) {
        const year = date.getFullYear();
        const month = prependZeroToDigit(date.getMonth() + 1);
        const dayOfMonth = prependZeroToDigit(date.getDate());

        return year + '-' + month + '-' + dayOfMonth;
    },
    prettyString: function(date: Date) {
        const year = date.getFullYear();
        const month = date.getMonth();
        const dayOfMonth = date.getDate();
        const dayOfWeek = date.getDay();

        return dayString[dayOfWeek] + ', ' + monthString[month] + ' ' + dayOfMonth + ', ' + year;
    }
};

function prependZeroToDigit(digit: number) {
    return digit < 10 ? '0' + digit : digit;
}
