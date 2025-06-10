import {format} from "date-fns";

export const formatDateTime = (dateString: string, pattern: string): string => {
    if(!dateString) return "";

    const date = new Date(dateString);
    return format(date, pattern);
}

export function getLocalISOString(date: Date): string {
    return format(date, "yyyy-MM-dd");
}

export function getRoundedDateISOString() {
    const date = new Date();
    date.setSeconds(0);
    date.setMilliseconds(0);

    const minutes = date.getMinutes();
    const roundedMinutes = Math.ceil(minutes / 15) * 15;
    if (roundedMinutes === 60) {
        date.setHours(date.getHours() + 1);
        date.setMinutes(0);
    } else {
        date.setMinutes(roundedMinutes);
    }

    return date.toISOString().slice(0, 16);
}