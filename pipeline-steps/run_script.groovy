def call(Map args){
    withEnv(args.environment_vars){

    }



}


def call2(Map args){
    common = load './rpc-gating/pipeline-steps/common.groovy'
    region_env = "RAX_REGION=${args.region}"
    withEnv(['ANSIBLE_FORCE_COLOR=true', region_env]){
        withCredentials([
                file(
                        credentialsId: 'RPCJENKINS_RAXRC',
                        variable: 'RAX_CREDS_FILE'
                ),
                file(
                        credentialsId: 'id_rsa_cloud10_jenkins_file',
                        variable: 'JENKINS_SSH_PRIVKEY'
                )
        ]){
            dir("rpc-gating/playbooks"){
                common.install_ansible()
                common.venvPlaybook(
                        venv: ".venv",
                        args: [
                                "-e name=\"${args.nodeprefix}\"",
                                "-e flavor=\"${args.flavor}\"",
                                "-e image=\"${args.image}\"",
                                "-e count=\"${args.numnodes}\"",
                                "-e keyname=\"${args.keyname}\"",
                                "--private-key=\"${env.JENKINS_SSH_PRIVKEY}\"",
                                "allocate_pubcloud.yml"
                        ]
                )
                return result

            } // directory
        } //withCredentials
    } // withEnv
} //call
return this
