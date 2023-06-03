package pl.matrasbartosz.zadanieatipera.entity;

public class GitHubBranch {

    private String branchName;
    private String lastCommitSha;

    public GitHubBranch(String branchName, String lastCommitSha) {
        this.branchName = branchName;
        this.lastCommitSha = lastCommitSha;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }

    public void setLastCommitSha(String lastCommitSha) {
        this.lastCommitSha = lastCommitSha;
    }

    @Override
    public String toString() {
        return "GitHubBranch{" +
                "branchName='" + branchName + '\'' +
                ", lastCommitSha='" + lastCommitSha + '\'' +
                '}';
    }
}
