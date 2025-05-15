import './App.css'
import './ui/styles/global.css'
import './ui/styles/forms.css'
import {BrowserRouter} from "react-router-dom";
import RoutesConfig from "./auth/RoutesConfig.tsx";
import {UserProvider} from "./context/UserContext.tsx";
import Header from "./ui/components/layout/Header/Header.tsx";


function App() {

  return (
    <>
        <UserProvider>
            <BrowserRouter>
                <Header />
                <div className="app">
                    <RoutesConfig />
                </div>
            </BrowserRouter>
        </UserProvider>
    </>
  )
}

export default App
