import axiosInstance from "../../../axios/axiosInstance.ts";
import {useContext, useState} from "react";
import {useNavigate} from "react-router";
import {UserContext, useUser} from "../../../context/UserContext.tsx";

interface SignInData {
    email: string;
    password: string;
}

function SignInPage() {
    const { setCurrentUser } = useUser()

    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate()

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const signInData: SignInData = {
            email,
            password,
        };

        try {
            const response = await axiosInstance.post("/auth/signin", signInData);
            
            console.log("Login successful:", response.data);
            setCurrentUser(response.data);

            navigate("/")
        } catch (err) {
            console.error("Login failed:", err);
            setError("Invalid credentials. Please try again.");
        }
    };

    return (
        <>
            <section className="pt-5 mt-5">
                <div className="container mt-5">
                    <div className="row justify-content-center">
                        <div className="col-md-6">
                            <div className="floating-card p-5">
                                <h5 className="text-center fw-bold text-uppercase mb-4">Login</h5>
                                <div className="card-body">
                                    <form onSubmit={handleSubmit} method="post">
                                        <div className="mb-3 input-group">
                                            <span className="input-group-text"><i className="fa-solid fa-envelope"></i></span>
                                            <input
                                                type="email"
                                                id="email"
                                                name="username"
                                                className="form-control"
                                                placeholder="Email"
                                                required
                                                value={email}
                                                onChange={(e) => setEmail(e.target.value)}
                                            />
                                        </div>

                                        <div className="mb-3 input-group">
                                            <span className="input-group-text"><i className="fa-solid fa-lock"></i></span>
                                            <input
                                                type="password"
                                                id="password"
                                                name="password"
                                                className="form-control"
                                                placeholder="Password"
                                                required
                                                value={password}
                                                onChange={(e) => setPassword(e.target.value)}
                                            />
                                        </div>

                                        {error && <div className="alert alert-danger" role="alert">{error}</div>}

                                        <button type="submit" className="btn btn-primary w-100">Sign in</button>

                                        <p className="mt-3 text-center">
                                            Don't have an account? <a className="fw-bold text-dark">Create here</a>
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

export default SignInPage;