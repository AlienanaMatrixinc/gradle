val disableMe by tasks.creating {
    doLast {
        println("This should not be printed if the task is disabled.")
    }
}
disableMe.enabled = false
