package pl.wroc.pwr.student.acteditor.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MainWindow extends Window {

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void initialize() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FormLayout());
		shell.setSize(782, 625);
		shell.setText("Welcome to Act Editor");
		shell.setMaximized(true);
		
		Group group = new Group(shell, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 10);
		fd_group.right = new FormAttachment(100, -10);
		fd_group.bottom = new FormAttachment(100, -10);
		fd_group.left = new FormAttachment(0, 556);
		group.setLayoutData(fd_group);
		
		Combo combo = new Combo(group, SWT.NONE);
		combo.setBounds(10, 120, 180, 23);
		
		Button btnUtwrz = new Button(group, SWT.NONE);
		btnUtwrz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new DecisionWindow(display);
			}
		});
		btnUtwrz.setBounds(115, 149, 75, 25);
		btnUtwrz.setText("Utw\u00F3rz");
	}

	@Override
	protected void open() {
		shell.open();
	}

	@Override
	protected void dispose() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public Display getMainDisplay() {
		return display;
	}

}