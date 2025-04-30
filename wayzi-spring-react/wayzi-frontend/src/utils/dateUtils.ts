import {format} from "date-fns";

export const formatDateTime = (dateString: string, pattern: string): string => {
    if(!dateString) return "";

    const date = new Date(dateString);
    return format(date, pattern);
}

export function getLocalISOString(date: Date): string {
    return format(date, "yyyy-MM-dd");
}