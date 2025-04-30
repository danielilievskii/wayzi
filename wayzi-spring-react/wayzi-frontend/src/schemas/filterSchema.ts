import {z} from 'zod'

export const filterSchema = z.object({
    departureLocationId: z.number().optional().nullable(),
    departureLocationName: z.string().optional(),
    arrivalLocationId: z.number().optional().nullable(),
    arrivalLocationName: z.string().optional(),
    date: z.string().optional().nullable(),
    passengersNum: z.string().optional().nullable(),
})

export type FilterSchemaType = z.infer<typeof filterSchema>;