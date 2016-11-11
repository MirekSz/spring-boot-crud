var gulp = require('gulp');


gulp.task('watch-folder', [ 'copy-folder' ], function() {
	var watcher = gulp.watch([ './src/**/*', './pom.xml' ], [ 'copy-folder' ]);
	watcher.on('change', function(event) {
		console.log('File ' + event.path + ' was ' + event.type);
	});
});

gulp.task('copy-folder', function() {
	gulp.src([ './src/**/*', './pom.xml' ], {
		base : './'
	}).pipe(gulp.dest('c:/dest/app1')).pipe(gulp.dest('c:/dest/app2'));
});
