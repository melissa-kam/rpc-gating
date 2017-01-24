def call(Map args) {
    withEnv(args.environment_vars) {
        sh """
        #!/bin/bash
        sudo -E ./${args.script}
        """
    }
}
return this
