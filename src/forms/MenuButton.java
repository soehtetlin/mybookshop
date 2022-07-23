package forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

public class MenuButton extends JToggleButton{
	JPopupMenu popup;
	
	public MenuButton(String name, JPopupMenu menu){
      super(name);
      setBorder(null);
      setBorderPainted(false);
      setFont(new Font("Tahoma", Font.BOLD, 14));
      setForeground(SystemColor.window);
      setBackground(new Color(153, 51, 204));
      this.popup = menu;
      addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JToggleButton btn1= MenuButton.this;
			if(btn1.isSelected()) {
				popup.show(btn1, 0,btn1.getBounds().height);
			} else {
				popup.setVisible(false);
			}
		}
	});
      
    popup.addPopupMenuListener(new PopupMenuListener() {
		
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			// TODO Auto-generated method stub
			MenuButton.this.setSelected(false);
		}
		
		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
      
//      popup.addPopupMenuListener(new PopupMenuListener() {
//          @Override
//          public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
//          @Override
//          public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//              MenuButton.this.setSelected(false);
//          }
//          @Override
//          public void popupMenuCanceled(PopupMenuEvent e) {}
//      });
	}
}