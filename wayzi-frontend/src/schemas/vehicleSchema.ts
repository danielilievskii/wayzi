import {z} from 'zod'

export const vehicleSchema = z.object({
    brand: z.string().min(2, "Brand is required"),
    model: z.string().min(1, "Model is required"),
    color: z.enum(
        ['RED', 'BLUE', 'GREEN', 'YELLOW', 'BLACK', 'WHITE',
        'ORANGE', 'PURPLE', 'PINK', 'BROWN', 'GREY',
        ], { required_error: "Please select color" }),
    capacity: z.string().min(1, "Capacity must be at least 1"),
    type: z.enum(['AUTOMOBILE', 'MOTORBIKE', 'BUS', 'VAN'], { required_error: "Please select vehicle type" }),
})

export type VehicleSchemaType = z.infer<typeof vehicleSchema>;