import {z} from 'zod'

export const paginationSchema = z.object({
    pageNum: z.number().optional().nullable(),
    pageSize: z.number().optional().nullable(),
})

export type PaginationSchemaType = z.infer<typeof paginationSchema>;