import {danger, fail} from 'danger';

danger.git.commits.forEach((commit) => {
    if (commit.message.startsWith('fixup!')) {
        fail('Commit message contains "fixup".');
    }
});
