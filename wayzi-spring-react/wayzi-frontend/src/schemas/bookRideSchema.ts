import {z} from 'zod'

export const BookRideSchema = z.object({
    // stopRequestLocationName: z.string().min(1, { message: "Stop location is required" }),
    // stopRequestLocationId: z.string().min(1, { message: "Stop location is required" }),
    paymentMethod: z.enum(
        ['CASH', 'CARD'], { required_error: "Please select a payment type" }),
    bookedSeats: z.coerce.number().min(1, { message: "At least 1 seat must be booked" }),
    message: z.string().optional(),


});

export type BookRideSchemaType = z.infer<typeof BookRideSchema>;