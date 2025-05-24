import {useState} from "react";
import {useNavigate} from "react-router";
import {useUser} from "../../../context/UserContext.tsx";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {signUpSchema, SignUpSchemaType} from "../../../schemas/signUpSchema.ts";


function SignUpPage() {
    const { signUp } = useUser()

    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate()

    const {register, handleSubmit, reset, formState: { errors } } = useForm<SignUpSchemaType>({
        resolver: zodResolver(signUpSchema),
    });

    const onSignUp = async (data: SignUpSchemaType) => {
        const result = await signUp(data)
        if(result?.error) {
            setError(result.error)
        } else {
            navigate("/email-sent", { state: { email: data.email } })
        }

    };

    return (
        <>
            <section className="pt-5 mt-5">
                <div className="container mt-5">
                    <div className="row justify-content-center">
                        <div className="col-md-6">
                            <div className="floating-card p-5">
                                <h5 className="text-center fw-bold text-uppercase mb-4">Sign Up</h5>
                                {error && <div className="alert alert-danger border-0" role="alert">{error}</div>}

                                <div className="card-body">
                                    <form onSubmit={handleSubmit(onSignUp)} method="post">
                                        <div className="mb-3">
                                            <div className="input-group">
                                                <span className="input-group-text"><i className="fa-solid fa-user"></i></span>
                                                <input
                                                    type="text"
                                                    id="name"
                                                    className="form-control"
                                                    placeholder="Name"
                                                    {...register("name")}
                                                />
                                            </div>
                                            {errors.name && <p className="text-danger">{errors.name.message}</p>}
                                        </div>


                                        <div className="mb-3">
                                            <div className="input-group">
                                                <span className="input-group-text"><i className="fa-solid fa-envelope"></i></span>
                                                <input
                                                    type="email"
                                                    id="email"
                                                    className="form-control"
                                                    placeholder="Email"
                                                    {...register("email")}
                                                />
                                            </div>
                                            {errors.email && <p className="text-danger">{errors.email.message}</p>}
                                        </div>


                                        <div className="mb-3">
                                            <div className="input-group">
                                                <span className="input-group-text"><i className="fa-solid fa-lock"></i></span>
                                                <input
                                                    type="password"
                                                    id="password"
                                                    className="form-control"
                                                    placeholder="Password"
                                                    {...register("password")}
                                                />
                                            </div>
                                            {errors.password && <p className="text-danger">{errors.password.message}</p>}
                                        </div>


                                        <div className="mb-3">
                                            <div className="input-group">
                                                <span className="input-group-text"><i className="fa-solid fa-lock"></i></span>
                                                <input
                                                    type="password"
                                                    id="confirmPassword"
                                                    className="form-control"
                                                    placeholder="Confirm Password"
                                                    {...register("confirmPassword")}
                                                />
                                            </div>
                                            {errors.confirmPassword && <p className="text-danger">{errors.confirmPassword.message}</p>}

                                        </div>

                                        <button type="submit" className="btn btn-primary w-100">Sign up</button>

                                        <p className="mt-3 text-center">
                                            Already have an account? <a className="fw-bold text-dark cursor-pointer" onClick={() => navigate("/login")}>Sign in</a>
                                        </p>
                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}

export default SignUpPage;