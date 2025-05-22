import {z} from 'zod'

export const signUpSchema = z
    .object({
        name: z.string().min(2, "Name must be at least 2 characters long"),
        email: z.string().email("Please enter a valid email address"),
        password: z.string().min(5, "Password must be at least 5 characters long"),
        confirmPassword: z.string().min(5, "Please confirm your password"),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: "Passwords do not match",
        path: ["confirmPassword"],
    });

export type SignUpSchemaType = z.infer<typeof signUpSchema>;