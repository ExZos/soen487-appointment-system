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

export const dateConverter = {
    // For some reason, when converting a hyphenated date string to a Date using new Date(),
    //  timezone differences are taken into account (and can skew date values by a day).
    // This is not the case when using backslashed date strings, so that's why we replace first
    fromSQLDateString: function(str: string) {
        const backslashedStr = str.replace('-', '/');
        return new Date(backslashedStr);
    }
};

function prependZeroToDigit(digit: number) {
    return digit < 10 ? '0' + digit : digit;
}
