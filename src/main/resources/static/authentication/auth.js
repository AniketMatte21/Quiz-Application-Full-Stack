document.addEventListener("DOMContentLoaded",()=>{

    const loginBtn=document.querySelector(".login-btn");
    const signupBtn=document.querySelector(".sign-up-btn");
    const none=document.querySelectorAll(".none");
    const alterTitle=document.querySelector(".alter-title");
    const createAccBtn=document.querySelector(".createAcc-btn");
    const LoginAccBtn=document.querySelector(".LoginAcc-btn");
    const form=document.querySelector(".signup-form");
    const buttons=document.querySelectorAll(".buttons");
    const otp=document.querySelector(".otp");

    const verify=document.querySelector(".verify-otp");

    function loginModel()
    {
        none.forEach((n)=>{
            n.classList.add("display");
        })

        if(signupBtn.classList.contains("btn-style2"))
        {
            signupBtn.classList.remove("btn-style2");

        }
        signupBtn.classList.add("btn-style1");
        loginBtn.classList.add("btn-style2");
        alterTitle.innerHTML="Login to QuizMaster";
        localStorage.setItem("AuthMode","login");
    }

    function signupModel()
    {
        none.forEach((n)=>{
            n.classList.remove("display");
        })

        if(loginBtn.classList.contains("btn-style2"))
        {
            signupBtn.classList.add("btn-style2");
            loginBtn.classList.remove("btn-style2");
            alterTitle.innerHTML="Create your Account";
            LoginAccBtn.classList.add("display");
            createAccBtn.classList.remove("display");
            localStorage.setItem("AuthMode","signup");

        }
    }

    if (loginBtn && signupBtn !== null) {
        loginBtn.addEventListener("click", () => {
            window.location.href="auth.html?mode=login";
        });

        signupBtn.addEventListener("click", () => {
            window.location.href="auth.html?mode=signup";
        });

        const urlParams=new URLSearchParams(window.location.search);
        const mode=urlParams.get("mode");
        if(mode==="signup")
        {
            signupModel();
        }
        else
        {
            loginModel();
            createAccBtn.classList.add("display");
            LoginAccBtn.classList.remove("display");
        }

        LoginAccBtn.addEventListener("click", (e) => {

            loginModel();

            const name=document.querySelector(".username").value;
            const pass=document.querySelector(".password").value;


        });

        createAccBtn.addEventListener("click", async (e) => {

            e.preventDefault();
            document.querySelector(".overlay").style.display="flex";
            document.body.style.overflow="hidden";
            signupModel();
            const Name=document.querySelector(".username").value;
            const Email=document.querySelector(".email").value;
            const password=document.querySelector(".password").value;
            const confPass=document.querySelector(".conf-pass").value;

            if(Name==="" || Email ==="" || password ==="" || confPass==="")
            {
                document.querySelector(".overlay").style.display="none";
                document.body.style.overflow="auto";
                swal.fire({
                    text:"Please enter all your data",
                    position:'top-end'
                })
                return;
            }

            if(password!==confPass)
            {
                swal.fire({
                    text:"password & confirm password does not match",
                    position:'top-end'
                })
                document.querySelector(".overlay").style.display="none";
                document.body.style.overflow="auto";
                return;
            }
            const obj={
                username:Name,
                email:Email,
                password:password
            }
            try{

                let response=await fetch("http://localhost:8080/api/validate",
                    {
                        method:"POST",
                        headers:{
                            "Content-Type":"application/json"
                        },
                        body:JSON.stringify(obj)
                    }
                )
                document.querySelector(".overlay").style.display="none";
                document.body.style.overflow="auto";

                let result=await response.text();
                if(!response.ok)
                {
                    swal.fire({
                        text:result,
                        position:'top-end'
                    })

                    return;
                }

                swal.fire({
                    text:result,
                    position:'top-end'
                })

                form.classList.add("display");
                buttons.forEach((e)=>{
                    e.classList.add("display");
                })

                otp.classList.remove("display");


                verify.addEventListener("click",async ()=>{
                    const otpField=document.querySelector(".otp-field").value;

                    try{

                        response=await fetch(`http://localhost:8080/api/doSignup?Otp=${otpField}`,
                            {
                                method:"POST",
                                headers:{
                                    "Content-type":"application/json"
                                },
                                body:JSON.stringify(obj),
                                credentials:"include"
                            }
                        )
                        let res=await response.text();
                        if(!response.ok)
                        {
                            swal.fire({
                                text:res,
                                position:'top-end'
                            })
                            return;
                        }

                        Swal.fire({title: res,
                                icon: "success",
                                draggable: true,
                                position: 'top-end',

                        });
                        form.classList.remove("display");
                        loginModel();
                        buttons.forEach((e)=>{
                            e.classList.remove("display");
                        })
                        otp.classList.add("display");
                        LoginAccBtn.classList.remove("display");
                        createAccBtn.classList.add("display");


                    }catch(error)
                    {
                        console.log(error.message);
                    }

                })
            }
            catch(error)
            {
                console.error("Fetch error:", error.message);
            }

        });
    }

    const navLogin=localStorage.getItem("navLogin");
    if(navLogin==="login")
    {
        loginModel();
        localStorage.removeItem("navLogin");
        createAccBtn.classList.add("display");
        LoginAccBtn.classList.remove("display");
        localStorage.removeItem("navLogin");
    }

    const authLogin=localStorage.getItem("authLogin");
    if(authLogin==="login")
    {
        loginModel();
        localStorage.removeItem("authLogin");
    }


})