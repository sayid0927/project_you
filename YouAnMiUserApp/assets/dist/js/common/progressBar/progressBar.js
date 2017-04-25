/**
 * Author: 程哲振.
 * Project Name: xxx.
 * Creation Time: 2016/7/25.
 */
define(['ToProgress'], function (ToProgress) {
    function progressBar(time) {
        var options = {
            id: 'top-progress-bar',
            color: '#F44336',
            height: '2px',
            duration: 0.2
        };
        var progressBar = new ToProgress(options);
        progressBar.increase(time);
    }

    return progressBar;
});