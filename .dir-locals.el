;;; Directory Local Variables
;;; For more information see (info "(emacs) Directory Variables")

((nil
  (eval setenv "CLASSPATH"
	(concat
	 (expand-file-name "/usr/share/java/sqlite-jdbc.jar:")
	 (let
	     ((l
	       (dir-locals-find-file
		(or
		 (buffer-file-name)
		 default-directory))))
	   (if
	       (listp l)
	       (car l)
	     l))
	 "target/classes"))))
