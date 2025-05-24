import {useState} from "react";
import {useNavigate} from "react-router";
import {useUser} from "../../../context/UserContext.tsx";
import {useLocation} from "react-router-dom";

interface SignInData {
    email: string;
    password: string;
}

function SignInPage() {
    const { signIn } = useUser()

    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate()
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";

    //TODO: Refactor this to use react-hook-form
    const onSignIn = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const signInData: SignInData = {
            email,
            password,
        };

        const result = await signIn(signInData)
        if(result?.error) {
            setError(result.error)
        } else {
            navigate(from, { replace: true });
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
                                    <form onSubmit={onSignIn} method="post">
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

                                        {error && <div className="alert alert-danger border-0" role="alert">{error}</div>}

                                        <button type="submit" className="btn btn-primary w-100">Sign in</button>

                                        <p className="mt-3 text-center">
                                            Don't have an account? <a className="fw-bold text-dark cursor-pointer" onClick={() => navigate("/register")}>Create here</a>
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