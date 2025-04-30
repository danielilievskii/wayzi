import {useUser} from "../context/UserContext.tsx";
import '../styles/home.css'

export const HomePage = () => {
    const { currentUser } = useUser()

    return (
        <div className="landing-page">

            {/*<h1>Hello {currentUser?.email ?? "Guest"}</h1>*/}


            <div className="container mt-4">
                <div className="row g-4 d-flex align-items-center justify-content-between">
                    <div className="col-md-6 mt-0">
                        <h1 className="fw-bold">Ride-Sharing Across Macedonia</h1>
                        <p>Travel smarter with our nationwide ride-sharing platform. Connect with drivers and passengers
                            across
                            Macedonia, save on travel costs, and enjoy a more convenient way to get around.</p>
                    </div>
                    <div className="col-md-6 d-md-block mt-0" >
                        <div className="landing-pic"></div>
                    </div>
                </div>

                {/*<div className="row mb-4">*/}
                {/*    <div th:replace="~{/fragments/rides-filter-form}"></div>*/}
                {/*</div>*/}

                <div className="row row-gap-4 mt-3 px-5">
                    <div className="col-md-4">
                        <div className="landing-page-card d-flex flex-column gap-3">
                            <i className="fa-solid fa-coins text-center d-block fa-2x"></i>
                            <div>
                                <h5>Find Your Ride, Your Way</h5>
                                <p>
                                    Whether you're commuting to class, heading home for the weekend, or planning a trip
                                    across
                                    Macedonia, our platform connects you with reliable drivers and affordable rides.
                                    Choose from
                                    a
                                    variety of routes and travel comfortably while saving money.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="landing-page-card d-flex flex-column gap-3">
                            <i className="fa-solid fa-shield text-center d-block fa-2x"></i>
                            <div>
                                <h5>Ride with Confidence</h5>
                                <p>
                                    Your safety and trust matter. That’s why we verify our drivers and encourage reviews
                                    from
                                    passengers, so you always know who you're sharing the journey with. Plus, with our
                                    secure
                                    booking system, you can confirm your ride with ease.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="landing-page-card d-flex flex-column gap-3">
                            <i className="fa-solid fa-bolt text-center d-block fa-2x"></i>
                            <div>
                                <h5>Simple, Fast, and Hassle-Free</h5>
                                <p>
                                    No more waiting or complicated travel plans. With just a few taps, find a ride near
                                    you,
                                    book
                                    your seat, and hit the road. Our intuitive app ensures a smooth experience, so you
                                    can focus
                                    on
                                    the journey ahead.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}