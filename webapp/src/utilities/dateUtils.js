export const monthString = {
    0: 'January',
    1: 'February',
    2: 'March',
    3: 'April',
    4: 'May',
    5: 'June',
    6: 'July',
    7: 'August',
    8: 'September',
    9: 'October',
    10: 'November',
    11: 'December'
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

        console.log(month);

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
