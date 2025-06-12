import {z} from 'zod'

export const EditRideSchema = z.object({
    departureLocationName: z.string().min(1, { message: "Departure location is required" }),
    departureLocationId: z.string().min(1, { message: "Departure location is required" }),
    departureAddress: z.string().min(1, { message: "Departure address is required" }),
    departureTime: z.string().min(1, { message: "Departure time is required" }),
    arrivalLocationName: z.string().min(1, { message: "Arrival location is required" }),
    arrivalLocationId: z.string().min(1, { message: "Arrival location is required" }),
    arrivalAddress: z.string().min(1, { message: "Arrival address is required" }),
    arrivalTime: z.string().min(1, { message: "Arrival time is required" }),
    vehicleId: z.string().min(1, { message: "Please select a vehicle type" }),
    availableSeats: z.coerce.number().min(1, { message: "At least 1 seat must be available" }),
    pricePerSeat: z.coerce.number().min(1, { message: "Price per seat is required" }),
    rideStops: z.array(
        z.object({
            id: z.string().nullable(),
            locationName: z.string().min(1, { message: "Please select a stop location" }),
            locationId: z.string().min(1, { message: "Stop location ID is required" }),
            stopAddress: z.string().min(1, { message: "Stop address is required" }),
            stopTime: z.string().min(1, { message: "Stop time is required" }),
        })
    ).optional()
});

export type EditRideSchemaType = z.infer<typeof EditRideSchema>;