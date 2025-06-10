import './App.css'
import './ui/styles/global.css'
import './ui/styles/forms.css'
import {BrowserRouter} from "react-router-dom";
import RoutesConfig from "./auth/RoutesConfig.tsx";
import {UserProvider} from "./context/UserContext.tsx";
import Header from "./ui/components/layout/Header/Header.tsx";
import Footer from "./ui/components/layout/Footer/Footer.tsx";


function App() {

    return (
        <>
            <BrowserRouter>
                <UserProvider>
                    <Header/>
                    <div className="app">
                        <RoutesConfig/>
                    </div>
                </UserProvider>
            </BrowserRouter>
        </>
    )
}

export default App
